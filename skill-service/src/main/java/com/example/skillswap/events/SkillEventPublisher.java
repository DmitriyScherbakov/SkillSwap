package com.example.skillswap.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Публикует события о навыках в Kafka.
 *
 * <p>Отправка откладывается до успешного коммита транзакции. Раньше событие
 * уходило в Kafka сразу, ещё внутри транзакции: если бы {@code save} или коммит
 * упали, подписчики получили бы уведомление о навыке, которого в базе нет.
 * Такой рассинхрон между БД и брокером потом приходится чинить вручную.
 *
 * <p>Если транзакции нет (например, метод вызван вне transactional-контекста),
 * событие отправляется сразу — поведение остаётся прежним.
 */
@Component
public class SkillEventPublisher {

    private static final String TOPIC = "skill-events";

    private final KafkaTemplate<String, SkillCreatedEvent> kafkaTemplate;

    public SkillEventPublisher(KafkaTemplate<String, SkillCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishSkillCreated(SkillCreatedEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            // Сработает только после успешного коммита; при откате — не сработает.
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            send(event);
                        }
                    }
            );
        } else {
            send(event);
        }
    }

    private void send(SkillCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.skillId().toString(), event);
    }
}
