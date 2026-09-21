# Movie Ticket Management System (BookMyShow Clone)

An enterprise-grade Full Stack Web Application developed in Spring Boot, Thymeleaf, JPA, and Bootstrap 5. It features a modern dark UI theme and interactive seat reservations.

## Tech Stack
- **Backend:** Java 17+, Spring Boot 3.x, Spring Data JPA, Spring Validation, Thymeleaf Engine
- **Frontend:** HTML5, CSS3 (Vanilla CSS variables), Bootstrap 5, FontAwesome 6, Google Fonts (Outfit)
- **Database:** H2 Database (Default) or MySQL 8 (Selectable)

## Features Included
1. **User Authentication:** Session-based user sign-up, sign-in, and log-out (no heavy Spring Security configurations to break local setups).
2. **Featured Movies Carousel:** Gorgeous landing page featuring active movie banners, search bars, and genre filters.
3. **Show Schedules:** Dates selector panel and screen listings grouped by movie theatres and ticket pricing.
4. **Interactive Seat Booking:** BookMyShow-style seat grid. Seats are color-coded (Green: Available, Red: Booked, Blue: Selected, Gold: VIP). Validates against concurrent bookings and recalculates pricing multipliers transactionally.
5. **Checkout & Billing:** Cost breakdowns, billing receipts, and payment simulation selectors.
6. **Ticket Receipt & Barcode:** Final admissions gatepass with generated transaction IDs and scan barcodes.
7. **My Bookings Dashboard:** Customer profiles listing transaction histories and support for ticket cancellations.
8. **Admin Control Panel:** Total sales revenue dashboards and full CRUD management interfaces for Movies, Theatres, and Screens.
9. **Automatic Data Seeding:** Prefills default customers, admins, theatres, movies, screenings, and seat matrices on startup.

---

## How to Run the Application

### 1. Prerequisite
Ensure you have **Java 17 (or higher)** installed on your machine.

### 2. Run the Application
From the project root directory, run the Maven wrapper or Maven command:
- **If Maven is installed globally:**
  ```bash
  mvn spring-boot:run
  ```
- **If running locally via custom wrapper:**
  ```powershell
  mvnw spring-boot:run
  ```

### 3. Open in Browser
Once the console logs `Started MovieTicketManagementApplication`, navigate to:
```url
http://localhost:8080/
```

### 4. Demo Login Credentials
Use these preseeded accounts to test features immediately:
- **Regular Customer:**
  - Email: `user@gmail.com`
  - Password: `user123`
- **Administrator Panel:**
  - Email: `admin@gmail.com`
  - Password: `admin123`

### 5. Accessing H2 Console
If running on the default in-memory database, you can view the tables at:
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:movie_ticket_db`
- **Username:** `sa`
- **Password:** *(leave blank)*

---

## Relational Database Schema Design (MySQL)

By default, the application runs on H2. To swap to MySQL:
1. Open `src/main/resources/application.properties`.
2. Comment out the **H2 Database** section.
3. Uncomment and configure the **MySQL** section.
4. The database tables will be created automatically via Hibernate DDL auto-update.

---

## Security

Please refer to [SECURITY.md](SECURITY.md) for vulnerability reporting guidelines.

## Contributing

Contributions are welcome! Please review [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and development process.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

Developed and maintained by **[Krishna Patil](https://github.com/kriss2012)**.
