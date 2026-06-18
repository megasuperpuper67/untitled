package by.psu.service;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.request.ExcursionUpdateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.exception.ExcursionNotFoundException;
import by.psu.mapper.ExcursionMapper;
import by.psu.model.Excursion;
import by.psu.repository.ExcursionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TourServiceService {
    private final ExcursionRepository excursionRepository;
    private final ExcursionMapper excursionMapper;

    @Transactional
    public ExcursionResponse createExcursion(ExcursionCreateRequest request) {
        var excursion = excursionMapper.toEntity(request);
        var savedExcursion = excursionRepository.save(excursion);
        return excursionMapper.toResponse(savedExcursion);
    }

    public ExcursionResponse getExcursionById(Integer id) {
        var excursion = excursionRepository.findById(id)
                .orElseThrow(ExcursionNotFoundException::new);
        return excursionMapper.toResponse(excursion);
    }

    //  Обновление экскурсии
    @Transactional
    public ExcursionResponse updateExcursion(Integer id, ExcursionUpdateRequest request) {
        var excursion = excursionRepository.findById(id)
                .orElseThrow(ExcursionNotFoundException::new);

        excursion.setName(request.name());
        excursion.setPrice(request.price());
        excursion.setFrom(request.from());
        excursion.setTo(request.to());
        excursion.setGuideName(request.guideName());
        excursion.setExcursionType(request.excursionType());
        excursion.setLunchIncluded(request.lunchIncluded());

        var updatedExcursion = excursionRepository.save(excursion);
        return excursionMapper.toResponse(updatedExcursion);
    }

    // Удаление экскурсии
    @Transactional
    public void deleteExcursion(Integer id) {
        if (!excursionRepository.existsById(id)) {
            throw new ExcursionNotFoundException();
        }
        excursionRepository.deleteById(id);
    }

    public Page<ExcursionResponse> getExcursionPage(Pageable pageable) {
        return excursionRepository.findAll(pageable).map(excursionMapper::toResponse);
    }
}