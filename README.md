# ECOMMERCE Prices API

Spring Boot service that returns the applicable selling price for a product, brand and application date.

## Requirements

- Java 25
- Maven Wrapper included in the repository

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on port `8080`. H2 console is available at `http://localhost:8080/h2-console` using JDBC URL `jdbc:h2:mem:pricesdb`, user `sa`, and an empty password.

## API

`GET /api/v1/prices`

Required query parameters:

- `applicationDate`: ISO-8601 local date-time, for example `2020-06-14T16:00:00`
- `productId`: positive product identifier
- `brandId`: positive brand identifier

Example:

```http
GET /api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1
```

Response:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

If no price applies, the API returns `404` with a JSON error body. Invalid input is rejected by validation.

## Tests

```powershell
.\mvnw.cmd test
```

The integration test covers the five requested price-resolution scenarios. The unit test isolates the application use case. A Postman collection is available in `postman/prices-api.postman_collection.json`.

## Architecture

- `domain`: business model and outbound repository port.
- `application`: use case and application service.
- `infrastructure`: JPA adapter, HTTP controller and exception handling.

The database is H2 in-memory and is seeded at startup with the supplied price rows. When periods overlap, the JPA query selects the highest priority price; the newest start date is used as a deterministic secondary ordering criterion.

## Cache

Applicable prices are cached in-process using Spring Cache and its default Java `ConcurrentMapCache` implementation. The cache key contains the product ID, brand ID and application date. This avoids a repeated database query for identical requests while keeping the service free from external cache infrastructure.
