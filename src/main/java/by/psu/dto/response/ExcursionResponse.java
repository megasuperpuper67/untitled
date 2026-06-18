package by.psu.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(defaultValue = "Экскурсия")
public record ExcursionResponse(
        @Schema(description = "Имя гида", requiredMode = REQUIRED)
        String guideName,

        @Schema(description = "Тип экскурсии", requiredMode = REQUIRED)
        String excursionType,

        @Schema(description = "Включён ли завтрак", requiredMode = REQUIRED)
        boolean lunchIncluded,

        @Schema(description = "Идентификатор экскурсии", requiredMode = REQUIRED)
        Integer id,

        @Schema(description = "Наименование экскурсии", requiredMode = REQUIRED)
        String name,

        @Schema(description = "Стоимость экскурсии", requiredMode = REQUIRED)
        BigDecimal price,

        @Schema(description = "Дата начала экскурсии", requiredMode = REQUIRED)
        LocalDate from,

        @Schema(description = "Дата окончания экскурсии", requiredMode = REQUIRED)
        LocalDate to
) {
}
