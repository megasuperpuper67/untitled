package by.psu;

import by.psu.db.ConnectionManager;
import by.psu.db.JdbcHelper;
import by.psu.exception.TourServiceValidationException;
import by.psu.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String SEPARATOR = "=".repeat(60);

    public static void main(String[] args) {
        demonstrateDatabaseOperations();
        demonstrateModelClasses();
    }

    private static void demonstrateDatabaseOperations() {
        printSection("DATABASE OPERATIONS");

        try (ConnectionManager connectionManager = new ConnectionManager()) {
            var connection = connectionManager.getConnection();
            var metadata = connection.getMetaData();

            System.out.printf("Database: %s v%d.%d%n",
                    metadata.getDatabaseProductName(),
                    metadata.getDatabaseMajorVersion(),
                    metadata.getDatabaseMinorVersion());

            connection.setAutoCommit(false);
            JdbcHelper jdbcHelper = new JdbcHelper(connection);

            // Read single excursion
            System.out.println("\n--- Reading excursion by ID ---");
            var excursion = jdbcHelper.findExcursionById(1);
            System.out.println("Found: " + excursion);

            // Create and save new excursion
            System.out.println("\n--- Saving new excursion ---");
            var newExcursion = createSampleExcursion();
            jdbcHelper.saveExcursion(newExcursion);
            System.out.println("Saved: " + newExcursion);

            // Update excursion
            newExcursion.setLunchIncluded(false);
            newExcursion.setGuideName("Updated Guide");
            jdbcHelper.saveExcursion(newExcursion);
            System.out.println("Updated: " + newExcursion);

            // Read all excursions
            System.out.println("\n--- All excursions ---");
            var excursions = jdbcHelper.findAllExcursions();
            excursions.forEach(ex -> System.out.println("  " + ex));

            connection.commit();
            System.out.println("\nTransaction committed successfully!");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database operation failed", e);
        }
    }

    private static void demonstrateModelClasses() {
        printSection("MODEL CLASSES DEMONSTRATION");

        testHotelStay();
        testClientCreation();
        testClientValidation();
        testBookingCreation();
        testBookingOperations();
        testStatusTransitions();
        testBookingValidation();
        testUnsupportedRoomTypes();
        testBookingCancellation();
        testToString();
        testRoomTypes();
    }

    private static void testHotelStay() {
        printSection("HotelStay Test");

        HotelStay hotel = new HotelStay(
                1, "Luxury Hotel", new BigDecimal("5000"),
                LocalDate.of(2024, Month.JUNE, 1),
                LocalDate.of(2024, Month.JUNE, 10),
                5, 9, RoomType.SUITE
        );

        System.out.println(hotel);
        System.out.printf("Price for 2 persons: %.2f%n", hotel.calculateTotalPrice(2));
    }

    private static void testClientCreation() {
        printSection("Client Creation Test");

        try {
            Client client = new Client(
                    "John Smith",
                    "john.smith@example.com",
                    "+12345678901",
                    "AB12345678",
                    1500
            );

            System.out.println("Created: " + client);

            client.addLoyaltyPoints(500);
            System.out.printf("After adding 500 points: %d points, discount: %.0f%%%n",
                    client.getLoyaltyPoints(),
                    client.getDiscountRate().multiply(new BigDecimal("100")));

            System.out.println("Masked passport: " + client.getMaskedPassportNumber());

        } catch (TourServiceValidationException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }

    private static void testClientValidation() {
        printSection("Client Validation Test");

        testInvalidClient("SingleName", "bad-email", "+12345678901", "1234567890", 1500);
        testClientCreationWithInvalidEmail();
        testClientCreationWithInvalidPhone();
        testClientCreationWithInvalidPassport();
    }

    private static void testInvalidClient(String name, String email, String phone,
                                          String passport, int points) {
        try {
            new Client(name, email, phone, passport, points);
        } catch (TourServiceValidationException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }

    private static void testClientCreationWithInvalidEmail() {
        System.out.println("\n--- Invalid Email ---");
        try {
            new Client("John Doe", "invalid-email", "+12345678901", "1234567890", 100);
        } catch (TourServiceValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void testClientCreationWithInvalidPhone() {
        System.out.println("\n--- Invalid Phone ---");
        try {
            new Client("Jane Doe", "jane@example.com", "12345", "1234567890", 100);
        } catch (TourServiceValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void testClientCreationWithInvalidPassport() {
        System.out.println("\n--- Invalid Passport ---");
        try {
            new Client("Bob Smith", "bob@example.com", "+12345678901", "123", 100);
        } catch (TourServiceValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void testBookingCreation() {
        printSection("Booking Creation Test");

        try {
            Client client = createTestClient("Anna Smirnova", "anna@example.com",
                    "+12345678901", "9876543210", 750);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(createTestHotelStay("Seaside Hotel", "8000", 4, 5, RoomType.DOUBLE), 2);
            services.put(createTestFlight("Flight", "12000", "Moscow", "Sochi", "SU5678", true), 2);

            Booking booking = new Booking(client, services);

            System.out.printf("Booking created: #%s%n", booking.getBookingId());
            System.out.println("Client: " + booking.getClient().getFullName());
            System.out.println("Status: " + booking.getStatus());
            System.out.printf("Total price with discount: %.2f%n", booking.calculateTotalPrice());

        } catch (TourServiceValidationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testBookingOperations() {
        printSection("Booking Operations Test");

        try {
            Client client = createTestClient("Peter Sidorov", "peter@example.com",
                    "+12345678901", "5555555555", 1200);

            HotelStay hotel = createTestHotelStay("Grand Hotel", "10000", 5, 3, RoomType.SINGLE);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 1);

            Booking booking = new Booking(client, services);
            System.out.println("Initial services: " + booking.getServiceParticipants().size());

            // Add excursion
            Excursion excursion = createTestExcursion("Mountain Tour", "5000",
                    "Maria Petrova", "Mountain", false);
            booking.addService(excursion, 1);
            System.out.println("After adding excursion: " +
                    booking.getServiceParticipants().size() + " services");

            // Try to update with invalid participants
            try {
                booking.updateParticipants(hotel, 2);
                System.out.println("Updated participants: " +
                        booking.getServiceParticipants().get(hotel));
            } catch (TourServiceValidationException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            // Remove service
            booking.removeService(excursion);
            System.out.println("After removing excursion: " +
                    booking.getServiceParticipants().size() + " services");
            System.out.printf("Final total: %.2f%n", booking.calculateTotalPrice());

        } catch (TourServiceValidationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testStatusTransitions() {
        printSection("Status Transition Test");

        try {
            Client client = createTestClient("Elena Volkova", "elena@example.com",
                    "+12345678901", "1111111111", 3000);

            Flight flight = createTestFlight("Flight", "20000", "Moscow",
                    "St. Petersburg", "SU9876", false);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(flight, 1);

            Booking booking = new Booking(client, services);

            System.out.println("Initial status: " + booking.getStatus());
            System.out.println("Initial points: " + client.getLoyaltyPoints());

            booking.confirm();
            System.out.println("After confirm(): " + booking.getStatus());

            booking.complete();
            System.out.println("After complete(): " + booking.getStatus());
            System.out.println("Points after completion: " + client.getLoyaltyPoints());

        } catch (TourServiceValidationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testBookingValidation() {
        printSection("Booking Validation Test");

        testEmptyServices();
        testExceededCapacity();
        testUnavailableService();
    }

    private static void testEmptyServices() {
        System.out.println("\n--- Empty Services ---");
        try {
            Client client = createTestClient("Test", "test@example.com",
                    "+12345678901", "9999999999", 0);
            new Booking(client, new HashMap<>());
        } catch (TourServiceValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void testExceededCapacity() {
        System.out.println("\n--- Exceeded Room Capacity ---");
        try {
            Client client = createTestClient("Test", "test@example.com",
                    "+12345678901", "9999999999", 0);
            HotelStay hotel = createTestHotelStay("Test Hotel", "5000", 3, 1, RoomType.SINGLE);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 3);

            new Booking(client, services);
        } catch (TourServiceValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void testUnavailableService() {
        System.out.println("\n--- Unavailable Service ---");
        try {
            Client client = createTestClient("Test", "test@example.com",
                    "+12345678901", "9999999999", 0);

            Excursion excursion = new Excursion(
                    2, "Future Excursion", new BigDecimal("3000"),
                    LocalDate.now().plusDays(10),
                    LocalDate.now().plusDays(15),
                    "Guide", "Walking", false
            );

            Map<TourService, Integer> services = new HashMap<>();
            services.put(excursion, 2);

            new Booking(client, services);

        } catch (TourServiceValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void testUnsupportedRoomTypes() {
        printSection("Unsupported Room Types Test");

        try {
            Client client = createTestClient("Test", "test@example.com",
                    "+12345678901", "4444444444", 1000);

            HotelStay hotel = new HotelStay(
                    1, "Test Hotel", new BigDecimal("5000"),
                    LocalDate.now(), LocalDate.now().plusDays(1),
                    3, 1, RoomType.TWIN
            );

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);

            new Booking(client, services);

        } catch (TourServiceValidationException e) {
            System.out.println("Caught (TWIN room type): " + e.getMessage());
        }
    }

    private static void testBookingCancellation() {
        printSection("Booking Cancellation Test");

        try {
            Client client = createTestClient("Sergey Ivanov", "sergey@example.com",
                    "+12345678901", "7777777777", 500);

            Flight flight = createTestFlight("Flight", "15000", "Moscow",
                    "Sochi", "SU1111", true);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(flight, 1);

            Booking booking = new Booking(client, services);
            System.out.println("Initial status: " + booking.getStatus());

            booking.cancel();
            System.out.println("After cancel(): " + booking.getStatus());

        } catch (TourServiceValidationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testToString() {
        printSection("toString() Test");

        try {
            Client client = createTestClient("Maria Ivanova", "maria@example.com",
                    "+12345678901", "8888888888", 2000);

            HotelStay hotel = createTestHotelStay("Star Hotel", "12000", 5, 3, RoomType.DOUBLE);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);

            Booking booking = new Booking(client, services);
            System.out.println(booking);

        } catch (TourServiceValidationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testRoomTypes() {
        printSection("Room Types Test");

        System.out.println("Available room types:");
        for (RoomType type : RoomType.values()) {
            System.out.printf("  %-10s - %s (capacity: %d)%n",
                    type.name(), type.getDescription(), type.getCapacity());
        }

        System.out.println("\nCapacity check:");
        System.out.println("SINGLE can accommodate 1 guest: " + RoomType.SINGLE.canAccommodate(1));
        System.out.println("SINGLE can accommodate 2 guests: " + RoomType.SINGLE.canAccommodate(2));
        System.out.println("FAMILY can accommodate 4 guests: " + RoomType.FAMILY.canAccommodate(4));
    }

    // Helper methods
    private static Client createTestClient(String name, String email,
                                           String phone, String passport, int points) {
        return new Client(name, email, phone, passport, points);
    }

    private static HotelStay createTestHotelStay(String name, String price,
                                                 int stars, int nights, RoomType roomType) {
        return new HotelStay(
                null, name, new BigDecimal(price),
                LocalDate.now(), LocalDate.now().plusDays(nights),
                stars, nights, roomType
        );
    }

    private static Flight createTestFlight(String name, String price,
                                           String origin, String destination,
                                           String flightNumber, boolean baggage) {
        return new Flight(
                null, name, new BigDecimal(price),
                LocalDate.now(), LocalDate.now().plusDays(1),
                origin, destination, flightNumber, baggage
        );
    }

    private static Excursion createTestExcursion(String name, String price,
                                                 String guide, String type, boolean lunch) {
        return new Excursion(
                null, name, new BigDecimal(price),
                LocalDate.now(), LocalDate.now().plusDays(1),
                guide, type, lunch
        );
    }

    private static Excursion createSampleExcursion() {
        return new Excursion(
                null, "City Tour", new BigDecimal("150.00"),
                LocalDate.now(), LocalDate.now().plusDays(1),
                "John Guide", "Walking", true
        );
    }

    private static void printSection(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  " + title);
        System.out.println(SEPARATOR);
    }
}