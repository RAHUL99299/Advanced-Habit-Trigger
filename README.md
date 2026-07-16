# Habit Trigger Analyzer

> A full-stack web application that identifies which contextual triggers (mood, weather, location, time of day) most influence habit completion. Built with Java OOP (Spring Boot) + MySQL + React.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18 + Vite, React Router v6, Axios, Recharts |
| Backend | Java 17, Spring Boot 3, Spring Security + JWT |
| Database | MySQL 8 |
| DB Access | Spring Data JPA/Hibernate (CRUD) + raw JDBC (analytics) |
| Build | Maven (backend), npm (frontend) |

---

## Prerequisites

- Java 17+ JDK (`java -version`)
- Maven 3.8+ (`mvn -version`)
- MySQL 8 running locally
- Node.js 18+ and npm

---

## Database Setup

```sql
CREATE DATABASE habit_trigger_analyzer;
```

Then update `habit-backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/habit_trigger_analyzer?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

To load demo data with 60 days of pre-built habit logs:
```bash
mysql -u root -p habit_trigger_analyzer < habit-backend/src/main/resources/data.sql
```

Demo accounts: `alice@demo.com` / `password123` and `bob@demo.com` / `password123`

---

## Running the App

### 1. Start the Backend

```bash
cd habit-backend
mvn spring-boot:run
```

Backend runs at: **http://localhost:8080**

### 2. Start the Frontend

```bash
cd habit-frontend
npm install
npm run dev
```

Frontend runs at: **http://localhost:5173**

---

## API Reference

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/auth/register` | Create account, returns JWT |
| POST | `/api/auth/login` | Login, returns JWT |
| GET | `/api/habits` | List user's habits |
| POST | `/api/habits` | Create habit |
| PUT | `/api/habits/{id}` | Update habit |
| DELETE | `/api/habits/{id}` | Delete habit |
| POST | `/api/habits/{id}/logs` | Log a habit entry |
| GET | `/api/habits/{id}/logs` | Get log history |
| GET | `/api/habits/{id}/insights` | Run trigger analysis |
| GET | `/api/habits/{id}/analytics` | Streak + best/worst trigger (JDBC) |
| GET | `/api/dashboard/summary` | Aggregate stats |

---

## OOP Design (Viva Explanation)

### Abstraction + Encapsulation — `Trigger.java`

```java
public abstract class Trigger {
    private final String type;   // encapsulated — private field
    private final String value;

    public String getType() { return type; }   // getter only
    public String getValue() { return value; }

    // abstraction — subclasses must define this
    public abstract double calculateImpactScore(List<HabitLog> logs);
}
```

- `type` and `value` are **private** — cannot be modified externally (encapsulation)
- `calculateImpactScore()` is **abstract** — no implementation here, forces subclasses (abstraction)

### Inheritance — 4 Subclasses

```
Trigger (abstract)
 ├── MoodTrigger      extends Trigger
 ├── WeatherTrigger   extends Trigger
 ├── LocationTrigger  extends Trigger
 └── TimeTrigger      extends Trigger
```

Each subclass inherits `type`, `value`, `getType()`, `getValue()` from `Trigger` and only needs to implement `calculateImpactScore()`.

### Polymorphism — `InsightService.java`

```java
List<Trigger> triggers = buildTriggers(logs);  // contains all 4 subclasses

triggers.stream()
    .map(trigger -> trigger.calculateImpactScore(logs))  // ONE LINE, FOUR BEHAVIOURS
    ...
```

At runtime, Java dispatches to `WeatherTrigger.calculateImpactScore()`, `MoodTrigger.calculateImpactScore()`, etc. — without `InsightService` knowing which subclass it's calling.

### Interface — `Analyzable.java`

```java
public interface Analyzable {
    InsightResult analyze(List<HabitLog> logs);
}
```

`InsightService implements Analyzable` — means any class implementing `Analyzable` can be swapped in (e.g., an ML-based analyzer) without changing the controller.

### Raw JDBC — `AnalyticsJdbcDao.java`

Uses `java.sql.Connection`, `PreparedStatement`, and `ResultSet` directly — **no JPA/Hibernate** — demonstrating explicit JDBC skill:

```java
try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    stmt.setLong(1, habitId);
    ResultSet rs = stmt.executeQuery();
    ...
}
```

---

## Project Structure

```
habit-backend/
└── src/main/java/com/habittrigger/
    ├── model/           JPA entities
    ├── trigger/         OOP showcase (abstract class + 4 subclasses + interface)
    ├── repository/      Spring Data JPA repositories
    ├── jdbc/            Raw JDBC analytics (AnalyticsJdbcDao)
    ├── service/         Business logic
    ├── controller/      REST endpoints
    ├── dto/             Data transfer objects
    ├── config/          JWT + Security + CORS
    └── exception/       Global error handling

habit-frontend/
└── src/
    ├── api/             Axios client
    ├── context/         Auth context (JWT)
    ├── pages/           Login, Register, Dashboard, Habits, HabitDetail, LogHabit, Insights
    ├── components/      Navbar, HabitCard, StreakBadge, InsightChart, ProtectedRoute
    ├── styles/          global.css design system
    └── utils/           dateHelpers
```
