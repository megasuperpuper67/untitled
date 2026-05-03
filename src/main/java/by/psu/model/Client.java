package by.psu.model;

import by.psu.exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class Client {
    private final UUID clientId;
    private String fullName;
    private String email;
    private String phone;
    private String passportNumber;
    private int loyaltyPoints;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[\\w.+\\-]+@[\\w.\\-]+$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+\\d{10,15}$"
    );

    private static final int PASSPORT_LENGTH = 10;
    private static final int MIN_NAME_PARTS = 2;
    private static final int MIN_PART_LENGTH = 2;

    public Client(String fullName, String email, String phone,
                  String passportNumber, int loyaltyPoints) {
        this.clientId = UUID.randomUUID();
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
        setPassportNumber(passportNumber);
        setLoyaltyPoints(loyaltyPoints);
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        Objects.requireNonNull(fullName, "fullName cannot be null");

        String trimmed = fullName.trim();
        String[] parts = trimmed.split("\\s+");

        if (parts.length < MIN_NAME_PARTS) {
            throw new TourServiceValidationException(
                    String.format("Full name '%s' must contain at least %d words",
                            trimmed, MIN_NAME_PARTS)
            );
        }

        for (String part : parts) {
            if (part.length() < MIN_PART_LENGTH) {
                throw new TourServiceValidationException(
                        String.format("Each name part must be at least %d characters, got '%s'",
                                MIN_PART_LENGTH, part)
                );
            }
        }

        this.fullName = trimmed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new TourServiceValidationException(
                    String.format("Invalid email format: '%s'", email)
            );
        }
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new TourServiceValidationException(
                    String.format("Phone must start with '+' and contain 10-15 digits, got '%s'", phone)
            );
        }
        this.phone = phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.length() != PASSPORT_LENGTH) {
            throw new TourServiceValidationException(
                    String.format("Passport must be exactly %d characters, got %s",
                            PASSPORT_LENGTH, passportNumber)
            );
        }
        this.passportNumber = passportNumber;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        if (loyaltyPoints < 0) {
            throw new TourServiceValidationException(
                    String.format("Loyalty points cannot be negative, got %d", loyaltyPoints)
            );
        }
        this.loyaltyPoints = loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        setLoyaltyPoints(this.loyaltyPoints + points);
    }

    public BigDecimal getDiscountRate() {
        final int[] thresholds = {5000, 1000, 500, 100};
        final BigDecimal[] rates = {
                new BigDecimal("0.20"),
                new BigDecimal("0.15"),
                new BigDecimal("0.10"),
                new BigDecimal("0.05")
        };

        for (int i = 0; i < thresholds.length; i++) {
            if (loyaltyPoints >= thresholds[i]) {
                return rates[i];
            }
        }

        return BigDecimal.ZERO;
    }

    public String getMaskedPassportNumber() {
        if (passportNumber == null || passportNumber.length() < 4) {
            return "****";
        }

        int visibleCount = 4;
        int maskedCount = passportNumber.length() - visibleCount;

        return "*".repeat(maskedCount) +
                passportNumber.substring(maskedCount);
    }

    @Override
    public String toString() {
        return String.format(
                "Client{id=%s, name='%s', email='%s', phone='%s', " +
                        "passport='%s', points=%d, discount=%.0f%%}",
                clientId, fullName, email, phone,
                getMaskedPassportNumber(), loyaltyPoints,
                getDiscountRate().multiply(new BigDecimal("100"))
        );
    }
}