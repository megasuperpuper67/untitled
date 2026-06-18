package by.psu.model;

import java.math.BigDecimal;

public class HotelStay extends TourService  {
    private int stars;
    private int nights;
    RoomType roomType;

    public String toString() {
        return "User{Сколько звезд=\"" + stars + "\", Сколько ночей=\"" + nights + "\", Тип комнаты=\"" + (roomType != null ? roomType.name() : "null") + "\", Name=\"" + getName() + "\", From=\"" + getFrom() + "\", To=\"" + getTo() + "\", Price=\"" + getPrice() + "\"}";
    }
    public HotelStay(int stars, int nights,BigDecimal price,LocalDate from,LocalDate to){
        super(price,from,to);
        this.stars = stars;
        this.nights = nights;
        this.roomType=roomType;
    }
    public void setStars(int stars){
        this.stars = stars;
    }
    public int getStars(){
        return stars;
    }
    public void setNights(int nights){
        this.nights = nights;
    }
    public int getNights(){
        return nights;
    }
    public void setRoomtype(RoomType roomType){this.roomType = roomType;}
    public RoomType getRoomType(){return roomType;}



    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        return getPrice()
                .multiply(BigDecimal.valueOf(participants))
                .multiply(getStarMultiplier())
                .multiply(getNightMultiplier());
    }
    private BigDecimal getStarMultiplier() {
        return switch (stars) {
            case 0 -> new BigDecimal("1.0");
            case 1 -> new BigDecimal("1.1");
            case 2 -> new BigDecimal("1.2");
            case 3 -> new BigDecimal("1.3");
            case 4 -> new BigDecimal("1.4");
            case 5 -> new BigDecimal("1.5");
            default -> BigDecimal.ZERO;
        };
    }
    private BigDecimal getNightMultiplier(){
        return switch (nights){
            case 1 -> new BigDecimal("1.2");
            case 2 -> new BigDecimal("1.4");
            case 3 -> new BigDecimal("1.6");
            default -> new BigDecimal("2.0");
        };
    }
}
