package by.psu.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class Flight extends TourService {
    private String origin;
    private String destination;
    private String flightNumber;
    private boolean baggageIncluded;

    private static final BigDecimal BAGGAGE_MULTIPLIER = new BigDecimal("1.3");

    public Flight() {
        super();
    }

    public Flight(Integer id, String name, BigDecimal price,
                  LocalDate from, LocalDate to,
                  String origin, String destination,
                  String flightNumber, boolean baggageIncluded) {
        super(id, name, price, from, to);
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.baggageIncluded = baggageIncluded;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean isBaggageIncluded() {
        return baggageIncluded;
    }

    public void setBaggageIncluded(boolean baggageIncluded) {
        this.baggageIncluded = baggageIncluded;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal basePrice = getPrice().multiply(BigDecimal.valueOf(participants));

        return baggageIncluded ?
                basePrice.multiply(BAGGAGE_MULTIPLIER) :
                basePrice;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return String.format(
                "Flight{id=%d, name='%s', price=%s, period=%s to %s, " +
                        "route='%s -> %s', flightNumber='%s', baggage=%b}",
                getId(), getName(),
                getPrice() != null ? df.format(getPrice()) : "null",
                getFrom(), getTo(), origin, destination, flightNumber, baggageIncluded
        );
    }
}