package com.example.skillswap.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SkillDto(
        Long id,

        @NotBlank(message = "Название навыка обязательно")
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String title,

        @Size(max = 500, message = "Описание не должно превышать 500 символов")
        String description,

        String ownerName,

        Long ownerId
) {
}
