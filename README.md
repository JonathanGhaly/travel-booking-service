# Travel Booking Service

The **Travel Booking Service** is a core microservice within the Travel Platform responsible for managing user reservations at Points of Interest (POIs). It features robust role-based access control, Redis-based caching for high-performance reads, and an event-driven architecture that publishes state changes to Kafka.

## Technology Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Database:** PostgreSQL (with Flyway for migrations)
* **Caching:** Redis (Spring Cache)
* **Messaging:** Apache Kafka (Producer)
* **Security:** Spring Security, OAuth2 Resource Server (JWT via Keycloak)
* **Mapping & Boilerplate:** MapStruct, Lombok
* **Observability:** Micrometer, Prometheus, Spring Boot Actuator

---

## Key Features

1. **Role-Based Access Control:** Separate API controllers and service implementations (`@Qualifier("userBookingService")` vs `@Qualifier("adminBookingService")`) ensure users can only access their own data, while administrators have global oversight.
2. **Event-Driven Architecture:** Every state change (Create, Update, Delete) publishes a `BookingEventDto` to the `booking-events` Kafka topic, allowing asynchronous processing by downstream services (e.g., the Notification Service).
3. **High-Performance Reads:** Method-level caching using Spring `@Cacheable` and `@CacheEvict` backed by Redis ensures frequent queries (like a user fetching their bookings) do not hit the database unnecessarily.
4. **Automated Migrations:** Flyway ensures the PostgreSQL database schema is automatically versioned and migrated on startup.

---

## Configuration & Prerequisites

To run this service locally, you need the following infrastructure components:

* **PostgreSQL** (Default port: `5432`)
* **Redis** (Default port: `6379`)
* **Apache Kafka** (Default port: `9092`)
* **Keycloak** (Running locally on port `8080` with the `travel-app` realm)

### Environment Variables

You can override the default `application.yml` properties using the following environment variables:

| Variable | Description | Default |
| :--- | :--- | :--- |
| `SERVER_PORT` | Port the service runs on | `8083` |
| `DB_HOST` | PostgreSQL database host | `localhost` |
| `DB_PORT` | PostgreSQL database port | `5432` |
| `DB_NAME` | PostgreSQL database name | `travel_db` |
| `DB_USER` | PostgreSQL username | *None* |
| `DB_PASSWORD` | PostgreSQL password | *None* |
| `REDIS_HOST` | Redis server host | `localhost` |
| `REDIS_PORT` | Redis server port | `6379` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka broker URL | `localhost:9092` |

---

## API Endpoints

All endpoints require a valid JWT Bearer token in the `Authorization` header.

### User Endpoints (`ROLE_USER`)
*Strictly isolated to the authenticated user's ID (extracted from the JWT subject).*

* **GET** `/api/v1/bookings/user` - Get all bookings for the authenticated user.
* **GET** `/api/v1/bookings/user/{id}` - Get a specific booking.
* **GET** `/api/v1/bookings/user/by-poi?poiId={uuid}` - Get user's bookings for a specific POI.
* **POST** `/api/v1/bookings/user` - Create a new booking.
* **PUT** `/api/v1/bookings/user/{id}` - Fully update a booking.
* **PATCH** `/api/v1/bookings/user/{id}` - Partially update a booking (e.g., change status).
* **DELETE** `/api/v1/bookings/user/{id}` - Delete a booking.

### Admin Endpoints (`ROLE_ADMIN`)
*Global access to all system bookings.*

* **GET** `/api/v1/admin-bookings` - Get all bookings system-wide.
* **GET** `/api/v1/admin-bookings/{id}` - Get a specific booking by ID.
* **GET** `/api/v1/admin-bookings/by-user?userId={uuid}` - Get all bookings for a specific user.
* **GET** `/api/v1/admin-bookings/by-poi?poiId={uuid}` - Get all bookings for a specific POI.
* **POST** `/api/v1/admin-bookings` - Create a booking on behalf of any user.
* **PUT/PATCH** `/api/v1/admin-bookings/{id}` - Update any booking.
* **DELETE** `/api/v1/admin-bookings/{id}` - Delete any booking.

---

## Kafka Events

The service publishes messages to the `booking-events` topic using the booking ID as the message key.

**Message Payload (`BookingEventDto`):**
```json
{
  "bookingId": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "poiId": "555e4567-e89b-12d3-a456-426614174000",
  "status": "CREATED", 
  "timestamp": 1713620000000
}
```
*Valid `status` values include: `CREATED`, `UPDATED`, `DELETED`.*

---

## Running the Service

**Local Development**
Ensure Docker Compose is running your infrastructure, then start the service using Gradle:

```bash
./gradlew :travel-booking-service:bootRun
```

**Running Tests**
The service uses Testcontainers for true integration testing against ephemeral PostgreSQL databases. To run the test suite:

```bash
./gradlew :travel-booking-service:test
```