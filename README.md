# ZJU Tennis - Player Analysis Backend Service

A Spring Boot backend service for tennis player analysis and management.

## Technology Stack

- Java 17
- Spring Boot 2.7.5
- Spring Data JPA
- MySQL 8
- Maven
- Lombok

## Database Configuration

The application connects to a MySQL database named `zjualumni`. Update the database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/zjualumni
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Project Structure

```
zjutennis/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── zjutennis/
│   │   │           ├── ZjutennisApplication.java
│   │   │           ├── controller/
│   │   │           │   └── PlayerController.java
│   │   │           ├── service/
│   │   │           │   └── PlayerService.java
│   │   │           ├── repository/
│   │   │           │   └── PlayerRepository.java
│   │   │           ├── model/
│   │   │           │   └── Player.java
│   │   │           └── config/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── zjutennis/
│                   └── ZjutennisApplicationTests.java
└── pom.xml
```

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

### Player Management

- `GET /api/players` - Get all players
- `GET /api/players/{id}` - Get player by ID
- `GET /api/players/email/{email}` - Get player by email
- `GET /api/players/graduation-year/{year}` - Get players by graduation year
- `GET /api/players/city/{city}` - Get players by city
- `GET /api/players/utr/{minUtr}` - Get players with UTR rating >= minUtr
- `POST /api/players` - Create new player
- `PUT /api/players/{id}` - Update player
- `DELETE /api/players/{id}` - Delete player

## Database Schema

The `Player` entity includes the following fields:
- id (Primary Key)
- name
- email
- utrRating
- graduationYear
- major
- city
- country
- phoneNumber
- createdAt (auto-generated)
- updatedAt (auto-generated)

## Next Steps

1. Update database credentials in `application.properties`
2. Create the database schema (or enable `spring.jpa.hibernate.ddl-auto=update` for auto-creation)
3. Add additional entities and business logic as needed
4. Implement authentication and authorization
5. Add integration tests
6. Configure logging and monitoring
