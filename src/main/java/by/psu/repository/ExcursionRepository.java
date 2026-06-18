package by.psu.repository;

import by.psu.model.Excursion;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcursionRepository extends ListCrudRepository<Excursion, Integer>,
        ListPagingAndSortingRepository<Excursion, Integer> {
}
