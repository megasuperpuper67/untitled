package by.psu.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Booking {

    private final String bookingId;
    private final Client client;
    private final Map<TourService, Integer> serviceParticipants;
    private final LocalDate bookingDate;
    private BookingStatus status;

    public Booking(Client client, Map<TourService, Integer> serviceParticipants) {
        if (client == null) {
            throw new TourServiceValidationException("client не может быть null");
        }
        if (serviceParticipants == null || serviceParticipants.isEmpty()) {
            throw new TourServiceValidationException("serviceParticipants не может быть null или пустым");
        }

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            Integer participants = entry.getValue();

            if (service == null) {
                throw new TourServiceValidationException("Услуга в serviceParticipants не может быть null");
            }
            if (service.isAvailableOn(LocalDate.now())) {
                throw new TourServiceValidationException(
                        String.format("Услуга %s недоступна на сегодня", service.getName())
                );
            }
            if (participants == null || participants <= 0) {
                throw new TourServiceValidationException(
                        String.format("Количество участников для %s должно быть больше 0", service.getName())
                );
            }
            if (service instanceof HotelStay) {
                HotelStay hotel = (HotelStay) service;
                int maxParticipants = getMaxParticipantsForRoomType(hotel.getRoomType().toString());
                if (participants > maxParticipants) {
                    throw new TourServiceValidationException(
                            String.format("Для номера типа %s максимальное количество участников: %d",
                                    hotel.getRoomType(), maxParticipants)
                    );
                }
            }
        }

        this.bookingId = generateBookingId();
        this.client = client;
        this.serviceParticipants = new HashMap<>(serviceParticipants);
        this.bookingDate = LocalDate.now();
        this.status = BookingStatus.PENDING;
    }

    private String generateBookingId() {
        long timestamp = System.currentTimeMillis();
        int randomDigits = new Random().nextInt(10000);
        return String.format("BK%d%04d", timestamp, randomDigits);
    }

    private int getMaxParticipantsForRoomType(String roomType) {
        return switch (roomType.toUpperCase()) {
            case "SINGLE" -> 1;
            case "DOUBLE" -> 2;
            case "FAMILY" -> 4;
            default -> 1;
        };
    }

    public String getBookingId() {
        return bookingId;
    }

    public Client getClient() {
        return client;
    }

    public Map<TourService, Integer> getServiceParticipants() {
        return new HashMap<>(serviceParticipants);
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void addService(TourService service, int participants) {
        if (status != BookingStatus.PENDING) {
            throw new TourServiceValidationException(
                    String.format("Нельзя добавить услугу. Текущий статус: %s", status)
            );
        }
        if (service == null) {
            throw new TourServiceValidationException("Услуга не может быть null");
        }
        if (service.isAvailableOn(LocalDate.now())) {
            throw new TourServiceValidationException(
                    String.format("Услуга %s недоступна на сегодня", service.getName())
            );
        }
        if (participants <= 0) {throw new TourServiceValidationException("Количество участников должно быть больше 0");
        }
        if (service instanceof HotelStay) {
            HotelStay hotel = (HotelStay) service;
            int maxParticipants = getMaxParticipantsForRoomType(hotel.getRoomType().toString());
            if (participants > maxParticipants) {
                throw new TourServiceValidationException(
                        String.format("Для номера типа %s максимальное количество участников: %d",
                                hotel.getRoomType(), maxParticipants)
                );
            }
        }
        serviceParticipants.put(service, participants);
    }

    public void removeService(TourService service) {
        if (status != BookingStatus.PENDING) {
            throw new TourServiceValidationException(
                    String.format("Нельзя удалить услугу. Текущий статус: %s", status)
            );
        }
        if (service == null) {
            throw new TourServiceValidationException("Услуга не может быть null");
        }
        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException("Услуга не найдена в бронировании");
        }
        if (serviceParticipants.size() == 1) {
            throw new TourServiceValidationException("Нельзя удалить последнюю услугу");
        }
        serviceParticipants.remove(service);
    }

    public void updateParticipants(TourService service, int participants) {
        if (status != BookingStatus.PENDING) {
            throw new TourServiceValidationException(
                    String.format("Нельзя обновить количество участников. Текущий статус: %s", status)
            );
        }
        if (service == null) {
            throw new TourServiceValidationException("Услуга не может быть null");
        }
        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException("Услуга не найдена в бронировании");
        }
        if (participants <= 0) {
            throw new TourServiceValidationException("Количество участников должно быть больше 0");
        }
        if (service instanceof HotelStay) {
            HotelStay hotel = (HotelStay) service;
            int maxParticipants = getMaxParticipantsForRoomType(hotel.getRoomType().toString());
            if (participants > maxParticipants) {
                throw new TourServiceValidationException(
                        String.format("Для номера типа %s максимальное количество участников: %d",
                                hotel.getRoomType(), maxParticipants)
                );
            }
        }
        serviceParticipants.put(service, participants);
    }

    public double calculateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;  // ← изменили тип на BigDecimal

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            int participants = entry.getValue();


            totalPrice = totalPrice.add(
                    service.getPrice().multiply(BigDecimal.valueOf(participants))
            );
        }


        BigDecimal discount = totalPrice.multiply(
                client.getDiscountRate().divide(BigDecimal.valueOf(100))
        );

        totalPrice = totalPrice.subtract(discount);


        return totalPrice.doubleValue();
    }

    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new TourServiceValidationException(
                    String.format("Нельзя подтвердить бронирование. Текущий статус: %s", status)
            );
        }
        this.status = BookingStatus.CONFIRMED;
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new TourServiceValidationException(
                    String.format("Нельзя завершить бронирование. Текущий статус: %s", status)
            );
        }
        this.status = BookingStatus.COMPLETED;
        int loyaltyPoints = (int) Math.round(calculateTotalPrice() * 0.1);
        client.addLoyaltyPoints(loyaltyPoints);
    }

    public void cancel() {
        if (status != BookingStatus.PENDING && status != BookingStatus.CONFIRMED) {
            throw new TourServiceValidationException(String.format("Нельзя отменить бронирование. Текущий статус: %s", status)
            );
        }
        this.status = BookingStatus.CANCELLED;
    }
}