package by.psu;

import by.psu.db.ConnectionManager;
import by.psu.db.JdbcHelper;
import by.psu.model.Excursion;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {

        SpringApplication.run(Bootstrap.class, args);

        /*try (var connectionManager = new ConnectionManager()) {
            // get connection, print metadata
            var connection = connectionManager.getConnection();
            var metadata = connection.getMetaData();
            var infoString = "Database: " + metadata.getDatabaseProductName()
                    + "\nversion: " + metadata.getDatabaseMajorVersion() + '.' + metadata.getDatabaseMinorVersion();
            System.out.println(infoString);

            // begin transaction
            connection.setAutoCommit(false);

            // read from DB
            JdbcHelper jdbcHelper = new JdbcHelper(connection);
            var excursion = jdbcHelper.findExcursionById(1);
            System.out.println(excursion);

            // save (insert) to DB
            var newExcursion = new Excursion(null, "travel", new BigDecimal("123.45"),
                    LocalDate.now(), LocalDate.now().plusDays(2L), "F.E. Cgan",
                    "Auto", true);
            jdbcHelper.saveExcursion(newExcursion);

            // save (update) to DB
            newExcursion.setLunchIncluded(false);
            jdbcHelper.saveExcursion(newExcursion);

            // read all from DB
            var list = jdbcHelper.findAllExcursions();
            System.out.println(list);

            //end transaction
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/


        /*var hotelStay = new HotelStay();
        hotelStay.setId(1);
        hotelStay.setName("Отель Люкс");
        hotelStay.setPrice(new BigDecimal("5000"));
        hotelStay.setFrom(LocalDate.of(2024, Month.JUNE, 1));
        hotelStay.setTo(LocalDate.of(2024, Month.JUNE, 10));
        hotelStay.setStars(5);
        hotelStay.setNights(9);
        hotelStay.setRoomType(RoomType.SUITE);
        System.out.println(hotelStay);
        System.out.println("Цена для 2 человек: " + hotelStay.calculateTotalPrice(2));

        try {
            Client client = new Client(
                    "Иван Петров",
                    "ivan.petrov@example.com",
                    "+79123456789",
                    "1234567890",
                    1500
            );
            System.out.println(client);

            client.addLoyaltyPoints(500);
            System.out.println("После добавления 500 баллов: " + client.getLoyaltyPoints() + " баллов, скидка " +
                    client.getDiscountRate().multiply(new BigDecimal("100")) + "%");

            System.out.println("Маскированный паспорт: " + client.getMaskedPassportNumber());

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        try {
            new Client(
                    "Иван",
                    "invalid-email",
                    "+79123456789",
                    "1234567890",
                    1500
            );
        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        }

        System.out.println("Тест: Создание бронирования");
        try {
            Client bookingClient = new Client(
                    "Анна Смирнова",
                    "anna.smirnova@example.com",
                    "+79234567890",
                    "9876543210",
                    750
            );

            HotelStay hotel = new HotelStay();
            hotel.setId(1);
            hotel.setName("Отель Приморский");
            hotel.setPrice(new BigDecimal("8000"));
            hotel.setFrom(LocalDate.now());
            hotel.setTo(LocalDate.now().plusDays(5));
            hotel.setStars(4);
            hotel.setNights(5);
            hotel.setRoomType(RoomType.DOUBLE);

            Flight flight = new Flight();
            flight.setId(2);
            flight.setName("Авиаперелет");
            flight.setPrice(new BigDecimal("12000"));
            flight.setFrom(LocalDate.now());
            flight.setTo(LocalDate.now().plusDays(5));
            flight.setOrigin("Москва");
            flight.setDestination("Сочи");
            flight.setFlightNumber("SU5678");
            flight.setBaggageInclude(true);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);
            services.put(flight, 2);

            Booking booking = new Booking(bookingClient, services);
            System.out.println("Бронирование создано успешно!");
            System.out.println("ID бронирования: " + booking.getBookingId());
            System.out.println("Клиент: " + booking.getClient().getFullName());
            System.out.println("Статус: " + booking.getStatus());
            System.out.println("Итоговая сумма со скидкой: " + booking.calculateTotalPrice());

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТест: Операции с бронированием");
        try {
            Client bookingClient = new Client(
                    "Петр Сидоров",
                    "petr.sidorov@example.com",
                    "+79876543210",
                    "5555555555",
                    1200
            );

            HotelStay hotel = new HotelStay();
            hotel.setId(1);
            hotel.setName("Гранд Отель");
            hotel.setPrice(new BigDecimal("10000"));
            hotel.setFrom(LocalDate.now());
            hotel.setTo(LocalDate.now().plusDays(3));
            hotel.setStars(5);
            hotel.setNights(3);
            hotel.setRoomType(RoomType.SINGLE);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 1);

            Booking booking = new Booking(bookingClient, services);
            System.out.println("Начальное количество услуг: " + booking.getServiceParticipants().size());

            Excursion excursion = new Excursion();
            excursion.setId(2);
            excursion.setName("Горная экскурсия");
            excursion.setPrice(new BigDecimal("5000"));
            excursion.setFrom(LocalDate.now());
            excursion.setTo(LocalDate.now().plusDays(3));
            excursion.setGuideName("Мария Петрова");
            excursion.setExcursionType("Горная");
            excursion.setLunchIncluded(false);

            booking.addService(excursion, 1);
            System.out.println("После добавления экскурсии: " + booking.getServiceParticipants().size() + " услуг");

            try {
                booking.updateParticipants(hotel, 2);
                System.out.println("После обновления участников отеля: " + booking.getServiceParticipants().get(hotel) + " человека");
            } catch (TourServiceValidationException e) {
                System.out.println("Ожидаемая ошибка при обновлении: " + e.getMessage());
            }

            booking.removeService(excursion);
            System.out.println("После удаления экскурсии: " + booking.getServiceParticipants().size() + " услуг");
            System.out.println("Итоговая сумма: " + booking.calculateTotalPrice());

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТест: Переходы статусов");
        try {
            Client bookingClient = new Client(
                    "Елена Волкова",
                    "elena.volkova@example.com",
                    "+79001234567",
                    "1111111111",
                    3000
            );

            Flight flight = new Flight();
            flight.setId(1);
            flight.setName("Перелет");
            flight.setPrice(new BigDecimal("20000"));
            flight.setFrom(LocalDate.now());
            flight.setTo(LocalDate.now().plusDays(1));
            flight.setOrigin("Москва");
            flight.setDestination("Санкт-Петербург");
            flight.setFlightNumber("SU9876");
            flight.setBaggageInclude(false);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(flight, 1);

            Booking booking = new Booking(bookingClient, services);
            System.out.println("Начальный статус: " + booking.getStatus());
            System.out.println("Начальные баллы: " + bookingClient.getLoyaltyPoints());

            booking.confirm();
            System.out.println("После confirm(): " + booking.getStatus());

            booking.complete();
            System.out.println("После complete(): " + booking.getStatus());
            System.out.println("Баллы после начисления: " + bookingClient.getLoyaltyPoints());

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТест: Валидация бронирования");
        try {
            Client bookingClient = new Client(
                    "Ольга Николаева",
                    "olga@example.com",
                    "+79567891234",
                    "9999999999",
                    0
            );

            try {
                Map<TourService, Integer> emptyServices = new HashMap<>();
                new Booking(bookingClient, emptyServices);
            } catch (TourServiceValidationException e) {
                System.out.println("Ошибка (пустая карта): " + e.getMessage());
            }

            try {
                HotelStay hotel = new HotelStay();
                hotel.setId(1);
                hotel.setName("Отель");
                hotel.setPrice(new BigDecimal("5000"));
                hotel.setFrom(LocalDate.now());
                hotel.setTo(LocalDate.now().plusDays(1));
                hotel.setStars(3);
                hotel.setNights(1);
                hotel.setRoomType(RoomType.SINGLE);

                Map<TourService, Integer> services = new HashMap<>();
                services.put(hotel, 3);

                new Booking(bookingClient, services);
            } catch (TourServiceValidationException e) {
                System.out.println("Ошибка (превышение лимита участников): " + e.getMessage());
            }

            try {
                Excursion excursion = new Excursion();
                excursion.setId(2);
                excursion.setName("Экскурсия");
                excursion.setPrice(new BigDecimal("3000"));
                excursion.setFrom(LocalDate.now().plusDays(10));
                excursion.setTo(LocalDate.now().plusDays(15));
                excursion.setGuideName("Гид");
                excursion.setExcursionType("Пешая");
                excursion.setLunchIncluded(false);

                Map<TourService, Integer> services = new HashMap<>();
                services.put(excursion, 2);

                new Booking(bookingClient, services);
            } catch (TourServiceValidationException e) {
                System.out.println("Ошибка (услуга недоступна): " + e.getMessage());
            }

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТест: Неподдерживаемый тип номера");
        try {
            Client bookingClient = new Client(
                    "Тестовый Клиент",
                    "test@example.com",
                    "+79999999999",
                    "4444444444",
                    1000
            );

            HotelStay hotel = new HotelStay();
            hotel.setId(1);
            hotel.setName("Тестовый отель");
            hotel.setPrice(new BigDecimal("5000"));
            hotel.setFrom(LocalDate.now());
            hotel.setTo(LocalDate.now().plusDays(1));
            hotel.setStars(3);
            hotel.setNights(1);
            hotel.setRoomType(RoomType.TWIN);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);

            try {
                new Booking(bookingClient, services);
            } catch (TourServiceValidationException e) {
                System.out.println("Ошибка (неподдерживаемый тип номера TWIN): " + e.getMessage());
            }

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТест: Отмена бронирования");
        try {
            Client bookingClient = new Client(
                    "Сергей Иванов",
                    "sergey@example.com",
                    "+79111234567",
                    "7777777777",
                    500
            );

            Flight flight = new Flight();
            flight.setId(1);
            flight.setName("Перелет");
            flight.setPrice(new BigDecimal("15000"));
            flight.setFrom(LocalDate.now());
            flight.setTo(LocalDate.now().plusDays(1));
            flight.setOrigin("Москва");
            flight.setDestination("Сочи");
            flight.setFlightNumber("SU1111");
            flight.setBaggageInclude(true);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(flight, 1);

            Booking booking = new Booking(bookingClient, services);
            System.out.println("Статус: " + booking.getStatus());

            booking.cancel();
            System.out.println("После cancel(): " + booking.getStatus());

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nТест: toString()");
        try {
            Client bookingClient = new Client(
                    "Мария Иванова",
                    "maria@example.com",
                    "+79223334455",
                    "8888888888",
                    2000
            );

            HotelStay hotel = new HotelStay();
            hotel.setId(1);
            hotel.setName("Отель Звездный");
            hotel.setPrice(new BigDecimal("12000"));
            hotel.setFrom(LocalDate.now());
            hotel.setTo(LocalDate.now().plusDays(3));
            hotel.setStars(5);
            hotel.setNights(3);
            hotel.setRoomType(RoomType.DOUBLE);

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);

            Booking booking = new Booking(bookingClient, services);
            System.out.println(booking);

        } catch (TourServiceValidationException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }*/
    }
}
