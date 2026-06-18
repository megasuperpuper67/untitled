package by.psu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class TourService {
    @Id
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public TourService(String name, BigDecimal price, by.psu.model.LocalDate from, by.psu.model.LocalDate to) {
    }

    public TourService(BigDecimal price, by.psu.model.LocalDate from, by.psu.model.LocalDate to) {
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public boolean isAvailableOn(LocalDate date) {
        return !(from.isAfter(date) || to.isBefore(date));
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "TourService{" +
                "id=" + id +
                ", name='" + name +
                ", price=" + price +
                ", from=" + (from != null ? from.format(formatter) : "null") +
                ", to=" + (to != null ? to.format(formatter) : "null") +
                '}';
    }
}
