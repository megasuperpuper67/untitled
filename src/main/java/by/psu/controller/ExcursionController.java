package by.psu.controller;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.request.ExcursionUpdateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.service.TourServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "API для экскурсий")
@RestController
@RequestMapping("/api/v1/excursion")
@RequiredArgsConstructor
public class ExcursionController {
    private final TourServiceService tourServiceService;

    @Operation(summary = "Создаёт экскурсию")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ExcursionResponse create(@RequestBody @Valid ExcursionCreateRequest request) {
        return tourServiceService.createExcursion(request);
    }

    @Operation(summary = "Предоставляет экскурсию по ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ExcursionResponse getById(@PathVariable Integer id) {
        return tourServiceService.getExcursionById(id);
    }

    // Обновление экскурсии
    @Operation(summary = "Обновляет экскурсию по ID")
    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ExcursionResponse update(
            @PathVariable Integer id,
            @RequestBody @Valid ExcursionUpdateRequest request) {
        return tourServiceService.updateExcursion(id, request);
    }

    // Удаление экскурсии
    @Operation(summary = "Удаляет экскурсию по ID")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Integer id) {
        tourServiceService.deleteExcursion(id);
    }

    @Operation(summary = "Предоставляет список экскурсий")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Page<ExcursionResponse> getPage(@PageableDefault @ParameterObject Pageable pageable) {
        return tourServiceService.getExcursionPage(pageable);
    }
}