package by.psu.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class Excursion extends TourService {
    private String guideName;
    private String excursionType;
    private boolean lunchIncluded;

    private static final BigDecimal GROUP_DISCOUNT_THRESHOLD = BigDecimal.valueOf(10);
    private static final BigDecimal GROUP_DISCOUNT_RATE = new BigDecimal("0.90");
    private static final BigDecimal LUNCH_SURCHARGE_RATE = new BigDecimal("1.15");

    public Excursion() {
        super();
    }

    public Excursion(Integer id, String name, BigDecimal price,
                     LocalDate from, LocalDate to,
                     String guideName, String excursionType, boolean lunchIncluded) {
        super(id, name, price, from, to);
        this.guideName = guideName;
        this.excursionType = excursionType;
        this.lunchIncluded = lunchIncluded;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getExcursionType() {
        return excursionType;
    }

    public void setExcursionType(String excursionType) {
        this.excursionType = excursionType;
    }

    public boolean isLunchIncluded() {
        return lunchIncluded;
    }

    public void setLunchIncluded(boolean lunchIncluded) {
        this.lunchIncluded = lunchIncluded;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal participantsDecimal = BigDecimal.valueOf(participants);
        BigDecimal totalPrice = getPrice().multiply(participantsDecimal);

        if (participants > GROUP_DISCOUNT_THRESHOLD.intValue()) {
            totalPrice = totalPrice.multiply(GROUP_DISCOUNT_RATE);
        }

        if (lunchIncluded) {
            totalPrice = totalPrice.multiply(LUNCH_SURCHARGE_RATE);
        }

        return totalPrice;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return String.format(
                "Excursion{id=%d, name='%s', price=%s, period=%s to %s, " +
                        "guide='%s', type='%s', lunchIncluded=%b}",
                getId(), getName(),
                getPrice() != null ? df.format(getPrice()) : "null",
                getFrom(), getTo(), guideName, excursionType, lunchIncluded
        );
    }
}