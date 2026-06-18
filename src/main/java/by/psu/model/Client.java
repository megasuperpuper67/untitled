package by.psu.model;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

public class Client {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d{10,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final UUID clientId;
    private String fullName;
    private String email;
    private String phone;
    private String passportNumber;
    private int loyaltyPoints;

    public Client(String fullName, String email, String phone, String passportNumber, int loyaltyPoints) {
        this.clientId = UUID.randomUUID();

        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
        setPassportNumber(passportNumber);
        setLoyaltyPoints(loyaltyPoints);
    }

    public static boolean validateString(String fullName) {
        if (fullName == null) return false;

        String[] words = fullName.trim().split("\\s+");
        if (words.length < 2) return false;

        for (String word : words) {
            if (word.length() < 2) return false;
        }
        return true;
    }

    private int getLoyaltyTier(int points) {
        if (points < 0) return 0;
        if (points < 100) return 0;
        if (points < 500) return 1;
        if (points < 1000) return 2;
        if (points < 5000) return 3;
        return 4;
    }

    public BigDecimal getDiscountRate() {
        return switch (getLoyaltyTier(loyaltyPoints)) {
            case 1 -> BigDecimal.valueOf(5);
            case 2 -> BigDecimal.valueOf(10);
            case 3 -> BigDecimal.valueOf(15);
            case 4 -> BigDecimal.valueOf(20);
            default -> BigDecimal.ZERO;
        };
    }

    public void addLoyaltyPoints(int points) {
        if (points < 0) {
            throw new TourServiceValidationException(
                    String.format("points=%d (количество добавляемых баллов не может быть отрицательным)", points)
            );
        }
        this.loyaltyPoints += points;
    }

    public String getMaskedPassportNumber() {
        if (passportNumber == null || passportNumber.length() < 4) {
            return "****";
        }
        return "*".repeat(passportNumber.length() - 4) + passportNumber.substring(passportNumber.length() - 4);
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (!validateString(fullName)) {
            throw new TourServiceValidationException(
                    String.format("fullName=%s (должно содержать минимум 2 слова, каждое от 2 символов)", fullName)
            );
        }
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new TourServiceValidationException(
                    String.format("email=%s (некорректный формат email)", email)
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
                    String.format("phone=%s (должен начинаться с + и содержать от 10 до 15 цифр)", phone)
            );
        }
        this.phone = phone;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.length() != 10) {
            throw new TourServiceValidationException(
                    String.format("passportNumber=%s (должен быть не null и содержать ровно 10 символов)", passportNumber)
            );
        }
        this.passportNumber = passportNumber;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;}

    public void setLoyaltyPoints(int loyaltyPoints) {
        if (loyaltyPoints < 0) {
            throw new TourServiceValidationException(
                    String.format("loyaltyPoints=%d (не может быть отрицательным)", loyaltyPoints)
            );
        }
        this.loyaltyPoints = loyaltyPoints;
    }
}







