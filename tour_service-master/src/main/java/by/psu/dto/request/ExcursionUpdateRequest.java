package by.psu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExcursionUpdateRequest(
        @NotBlank(message = "Имя гида обязательно")
        String guideName,

        @NotBlank(message = "Тип экскурсии обязателен")
        String excursionType,

        @NotNull(message = "Поле lunchIncluded обязательно")
        Boolean lunchIncluded,

        @NotBlank(message = "Название экскурсии обязательно")
        String name,

        @NotNull(message = "Цена обязательна")
        @Positive(message = "Цена должна быть положительной")
        BigDecimal price,

        @NotNull(message = "Дата начала обязательна")
        LocalDate from,

        @NotNull(message = "Дата окончания обязательна")
        LocalDate to
) {
}