package by.psu.model;

import by.psu.exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Booking {
    private final String bookingId;
    private final Client client;
    private final Map<TourService, Integer> serviceParticipants;
    private final LocalDate bookingDate;
    private BookingStatus status;

    private static final String ID_PREFIX = "BK";
    private static final int ID_LENGTH = 8;
    private static final BigDecimal LOYALTY_MULTIPLIER = new BigDecimal("0.1");

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        COMPLETED,
        CANCELLED;

        public boolean canTransitionTo(BookingStatus target) {
            return switch (this) {
                case PENDING -> target == CONFIRMED || target == CANCELLED;
                case CONFIRMED -> target == COMPLETED || target == CANCELLED;
                case COMPLETED, CANCELLED -> false;
            };
        }
    }

    private static final Map<RoomType, Integer> ROOM_CAPACITY =
            new EnumMap<>(RoomType.class);

    static {
        ROOM_CAPACITY.put(RoomType.SINGLE, 1);
        ROOM_CAPACITY.put(RoomType.DOUBLE, 2);
        ROOM_CAPACITY.put(RoomType.FAMILY, 4);
    }

    public Booking(Client client, Map<TourService, Integer> serviceParticipants) {
        validateInputs(client, serviceParticipants);
        validateServices(serviceParticipants);

        this.client = client;
        this.serviceParticipants = new LinkedHashMap<>(serviceParticipants);
        this.bookingDate = LocalDate.now();
        this.status = BookingStatus.PENDING;
        this.bookingId = generateBookingId();
    }

    private void validateInputs(Client client, Map<TourService, Integer> services) {
        if (client == null) {
            throw new TourServiceValidationException("Client cannot be null");
        }
        if (services == null || services.isEmpty()) {
            throw new TourServiceValidationException("Services map cannot be null or empty");
        }
    }

    private void validateServices(Map<TourService, Integer> services) {
        LocalDate today = LocalDate.now();

        services.forEach((service, participants) -> {
            validateService(service);
            validateParticipants(participants, service);
            validateAvailability(service, today);
            validateRoomCapacity(service, participants);
        });
    }

    private void validateService(TourService service) {
        if (service == null) {
            throw new TourServiceValidationException("Service cannot be null");
        }
    }

    private void validateParticipants(Integer participants, TourService service) {
        if (participants == null || participants <= 0) {
            throw new TourServiceValidationException(
                    String.format("Invalid participants count %d for service '%s'",
                            participants, service.getName())
            );
        }
    }

    private void validateAvailability(TourService service, LocalDate date) {
        if (!service.isAvailableOn(date)) {
            throw new TourServiceValidationException(
                    String.format("Service '%s' is not available on %s",
                            service.getName(), date)
            );
        }
    }

    private void validateRoomCapacity(TourService service, int participants) {
        if (service instanceof HotelStay hotel) {
            int maxCapacity = getMaxParticipants(hotel.getRoomType());
            if (participants > maxCapacity) {
                throw new TourServiceValidationException(
                        String.format(
                                "HotelStay room type %s allows maximum %d participants, got %d",
                                hotel.getRoomType(), maxCapacity, participants
                        )
                );
            }
        }
    }

    private int getMaxParticipants(RoomType roomType) {
        return ROOM_CAPACITY.getOrDefault(roomType, 1);
    }

    private String generateBookingId() {
        return ID_PREFIX + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, ID_LENGTH)
                .toUpperCase();
    }

    public String getBookingId() {
        return bookingId;
    }

    public Client getClient() {
        return client;
    }

    public Map<TourService, Integer> getServiceParticipants() {
        return Collections.unmodifiableMap(serviceParticipants);
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void addService(TourService service, int participants) {
        validateService(service);
        validateParticipants(participants, service);
        validateAvailability(service, LocalDate.now());
        validateRoomCapacity(service, participants);

        serviceParticipants.put(service, participants);
    }

    public void removeService(TourService service) {
        validateService(service);

        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException(
                    String.format("Service '%s' not found in booking", service.getName())
            );
        }

        serviceParticipants.remove(service);
    }

    public void updateParticipants(TourService service, int participants) {
        validateService(service);
        validateParticipants(participants, service);

        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException(
                    String.format("Service '%s' not found in booking", service.getName())
            );
        }

        validateRoomCapacity(service, participants);
        serviceParticipants.put(service, participants);
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal total = serviceParticipants.entrySet().stream()
                .map(entry -> entry.getKey().calculateTotalPrice(entry.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = total.multiply(client.getDiscountRate());
        return total.subtract(discount);
    }

    public void confirm() {
        transitionTo(BookingStatus.CONFIRMED);
    }

    public void complete() {
        transitionTo(BookingStatus.COMPLETED);

        BigDecimal totalPrice = calculateTotalPrice();
        int loyaltyPoints = totalPrice.multiply(LOYALTY_MULTIPLIER).intValue();
        client.addLoyaltyPoints(loyaltyPoints);
    }

    public void cancel() {
        transitionTo(BookingStatus.CANCELLED);
    }

    private void transitionTo(BookingStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new TourServiceValidationException(
                    String.format("Cannot transition from %s to %s", status, newStatus)
            );
        }
        this.status = newStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Booking #%s%n", bookingId));
        builder.append(String.format("  Client: %s%n", client.getFullName()));
        builder.append(String.format("  Date: %s%n", bookingDate));
        builder.append(String.format("  Status: %s%n", status));
        builder.append("  Services:%n");

        serviceParticipants.forEach((service, count) ->
                builder.append(String.format("    - %s: %d participants%n",
                        service.getName(), count))
        );

        builder.append(String.format("  Total: %.2f%n", calculateTotalPrice()));

        return builder.toString();
    }
}