package by.psu.model;

import java.math.BigDecimal;

public class Flight extends TourService {
    private String  origin;
    private String destination;
    private String flightNumber;
    private boolean baggageIncluded;

    public Flight(String name,BigDecimal price,LocalDate from,LocalDate to,String  origin,String destination,String flightNumber,boolean baggageIncluded){
        super(name, price,from,to);
        this.origin=origin;
        this.destination=destination;
        this.flightNumber=flightNumber;
        this.baggageIncluded=baggageIncluded;


    }

    public void setOrigin( String origin){this.origin=origin;}

    public String  GetOrigin(){return origin;}

    public void setDestination( String destination){this.destination=destination;}

    public String  GetDestination(){return destination;}

    public void setFlightNumber( String flightNumber){this.flightNumber=flightNumber;}

    public String  GetFlightNumber(){return flightNumber;}

    public void setBaggageIncluded( boolean baggageIncluded){this.baggageIncluded=baggageIncluded;}

    public boolean  GetBaggageIncluded(){return baggageIncluded;}

    public String toString() {
        return "User{Источник=\"" + origin + "\", Место назначения=\"" + destination + "\", Номер рейса=\"" + flightNumber + "\", Багаж \"" +  baggageIncluded + "\", Name=\"" + getName() + "\", From=\"" + getFrom() + "\", To=\"" + getTo() + "\", Price=\"" + getPrice() + "\"}";
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        
        var totalPrice = getPrice().multiply(BigDecimal.valueOf(participants));
        return baggageIncluded ? totalPrice.multiply(new BigDecimal("1.3")) : totalPrice;
    }
}
