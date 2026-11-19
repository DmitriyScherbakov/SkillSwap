package com.example.skillswap.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

/**
 * Строит ключ кэша из всех значимых частей аргументов метода.
 *
 * <p>Ключ собирается явно, а не через {@code Pageable.toString()}: формат
 * {@code toString()} не является контрактом и менялся между версиями
 * Spring Data. Здесь в ключ попадают номер страницы, размер и полный
 * список сортировок (поле + направление), поэтому
 * {@code ?page=0&size=20&sort=id,asc} и {@code ?page=0&size=20&sort=title,desc}
 * дают разные записи кэша.
 */
@Component("pageableKeyGenerator")
public class PageableCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder(method.getName());
        for (Object param : params) {
            key.append('|');
            if (param instanceof Pageable pageable) {
                key.append(pageable.getPageNumber())
                   .append(':')
                   .append(pageable.getPageSize())
                   .append(':')
                   .append(sortToString(pageable.getSort()));
            } else {
                key.append(param);
            }
        }
        return key.toString();
    }

    private String sortToString(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return "unsorted";
        }
        return sort.stream()
                .map(order -> order.getProperty() + "," + order.getDirection().name())
                .collect(Collectors.joining(";"));
    }
}
