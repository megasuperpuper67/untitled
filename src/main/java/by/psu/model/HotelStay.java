package by.psu.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
import static java.util.Map.entry;

public class HotelStay extends TourService {
    private int stars;
    private int nights;
    private RoomType roomType;

    private static final Map<Integer, BigDecimal> STAR_MULTIPLIERS = Map.ofEntries(
            entry(0, new BigDecimal("1.0")),
            entry(1, new BigDecimal("1.1")),
            entry(2, new BigDecimal("1.2")),
            entry(3, new BigDecimal("1.4")),
            entry(4, new BigDecimal("1.6")),
            entry(5, new BigDecimal("1.8"))
    );

    private static final Map<Integer, BigDecimal> NIGHT_MULTIPLIERS = Map.ofEntries(
            entry(1, new BigDecimal("1.2")),
            entry(2, new BigDecimal("1.4")),
            entry(3, new BigDecimal("1.6"))
    );

    private static final BigDecimal DEFAULT_NIGHT_MULTIPLIER = new BigDecimal("2.0");

    public HotelStay() {
        super();
    }

    public HotelStay(Integer id, String name, BigDecimal price,
                     LocalDate from, LocalDate to,
                     int stars, int nights, RoomType roomType) {
        super(id, name, price, from, to);
        this.stars = stars;
        this.nights = nights;
        this.roomType = roomType;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal basePrice = getPrice().multiply(BigDecimal.valueOf(participants));
        BigDecimal starMultiplier = getStarMultiplier();
        BigDecimal nightMultiplier = getNightMultiplier();

        return basePrice.multiply(starMultiplier).multiply(nightMultiplier);
    }

    private BigDecimal getStarMultiplier() {
        return STAR_MULTIPLIERS.getOrDefault(stars, BigDecimal.ONE);
    }

    private BigDecimal getNightMultiplier() {
        return NIGHT_MULTIPLIERS.getOrDefault(nights, DEFAULT_NIGHT_MULTIPLIER);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return String.format(
                "HotelStay{id=%d, name='%s', price=%s, period=%s to %s, " +
                        "stars=%d, nights=%d, roomType=%s}",
                getId(), getName(),
                getPrice() != null ? df.format(getPrice()) : "null",
                getFrom(), getTo(), stars, nights, roomType
        );
    }
}