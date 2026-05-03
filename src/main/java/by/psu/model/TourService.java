package by.psu.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class TourService {
    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate validFrom;
    private LocalDate validTo;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    protected TourService() {
    }

    protected TourService(Integer id, String name, BigDecimal price,
                          LocalDate validFrom, LocalDate validTo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getFrom() {
        return validFrom;
    }

    public void setFrom(LocalDate from) {
        this.validFrom = from;
    }

    public LocalDate getTo() {
        return validTo;
    }

    public void setTo(LocalDate to) {
        this.validTo = to;
    }

    public boolean isAvailableOn(LocalDate date) {
        if (validFrom == null || validTo == null) {
            return false;
        }

        boolean isAfterStart = !date.isBefore(validFrom);
        boolean isBeforeEnd = !date.isAfter(validTo);

        return isAfterStart && isBeforeEnd;
    }

    @Override
    public String toString() {
        return String.format(
                "TourService{id=%d, name='%s', price=%s, period=%s - %s}",
                id, name, price,
                validFrom != null ? validFrom.format(DATE_FORMATTER) : "null",
                validTo != null ? validTo.format(DATE_FORMATTER) : "null"
        );
    }
}