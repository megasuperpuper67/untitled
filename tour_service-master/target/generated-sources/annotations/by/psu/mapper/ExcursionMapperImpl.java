package by.psu.mapper;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.request.ExcursionUpdateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.model.Excursion;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-18T22:57:39+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Microsoft)"
)
@Component
public class ExcursionMapperImpl implements ExcursionMapper {

    @Override
    public Excursion toEntity(ExcursionCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Excursion excursion = new Excursion();

        excursion.setName( request.name() );
        excursion.setPrice( request.price() );
        excursion.setFrom( request.from() );
        excursion.setTo( request.to() );
        excursion.setGuideName( request.guideName() );
        excursion.setExcursionType( request.excursionType() );
        if ( request.lunchIncluded() != null ) {
            excursion.setLunchIncluded( request.lunchIncluded() );
        }

        return excursion;
    }

    @Override
    public void updateEntity(Excursion excursion, ExcursionUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        excursion.setName( request.name() );
        excursion.setPrice( request.price() );
        excursion.setFrom( request.from() );
        excursion.setTo( request.to() );
        excursion.setGuideName( request.guideName() );
        excursion.setExcursionType( request.excursionType() );
        if ( request.lunchIncluded() != null ) {
            excursion.setLunchIncluded( request.lunchIncluded() );
        }
    }

    @Override
    public ExcursionResponse toResponse(Excursion excursion) {
        if ( excursion == null ) {
            return null;
        }

        String guideName = null;
        String excursionType = null;
        boolean lunchIncluded = false;
        Integer id = null;
        String name = null;
        BigDecimal price = null;
        LocalDate from = null;
        LocalDate to = null;

        guideName = excursion.getGuideName();
        excursionType = excursion.getExcursionType();
        lunchIncluded = excursion.isLunchIncluded();
        id = excursion.getId();
        name = excursion.getName();
        price = excursion.getPrice();
        from = excursion.getFrom();
        to = excursion.getTo();

        ExcursionResponse excursionResponse = new ExcursionResponse( guideName, excursionType, lunchIncluded, id, name, price, from, to );

        return excursionResponse;
    }
}
