package by.psu.mapper;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.request.ExcursionUpdateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.model.Excursion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ERROR)
public interface ExcursionMapper {
    @Mapping(target = "id", ignore = true)
    Excursion toEntity(ExcursionCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Excursion excursion, ExcursionUpdateRequest request);

    ExcursionResponse toResponse(Excursion excursion);
}