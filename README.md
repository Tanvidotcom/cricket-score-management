# Live Cricket Score Management System

A web-based cricket score management application built using **React, Spring Boot, and MySQL**. The application retrieves cricket match data from the CricAPI external service, stores match information in a MySQL database, and displays it through a responsive frontend.

## Features

- **Match Center:** View cricket matches stored in the database.
- **Live Scores:** Filter matches by live, upcoming, and completed status.
- **Match Search:** Search matches by team name, match title, or venue.
- **Match Details:** View match information and available score history.
- **Team Management:** Create, view, and manage cricket teams.
- **Player Management:** Add, view, and manage players associated with teams.
- **External Cricket API:** Fetch match data from CricAPI rather than relying on simulated scores.
- **Database Persistence:** Store match, team, and player information in MySQL using Spring Data JPA.
- **Match Synchronization:** Synchronize external match data with the local database.
- **Auto Refresh:** The frontend periodically checks the backend for updated match information.
- **Responsive UI:** Beige-themed interface designed for desktop and mobile screens.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 26 | Backend programming language |
| Spring Boot 4.1.1 | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Database access and ORM |
| Hibernate | Object-relational mapping |
| Maven | Backend build and dependency management |
| MySQL | Relational database |
| React | Frontend library |
| Vite 8.3.2 | Frontend development and build tool |
| JavaScript | Frontend programming language |
| Axios | HTTP requests from React |
| React Router DOM | Frontend navigation |
| Lucide React | UI icons |
| CricAPI | External cricket match data |
| CSS | Responsive styling |
| Node.js | 24.18.0 |

## Dependencies from Spring Initializr 
- Spring Web | Develop RESTful APIs and handle HTTP requests and response.
- Spring Data JPA | Simplify database operations using repositories and entities.
- MySQL Driver | Connect Spring Boot application to MySQL.
- Validation | Validate incoming request data using annotations.
- Spring Boot DevTools | Support faster development with automatic restarts.
- Lombok | Reduce boilerplate code such as getters, setters and constructors.

Additional libraries used:
- Jackson: process JSON data received from CricAPI. Spring Boot 4 uses Jackson 3 APIs, such as tools.jackson.databind.JsonNode
- Spring RestClient: make HTTP requests to the external CricAPI service
- Axios: communicate between the React frontend and Spring Boot backend.
- React Router DOM: navigate between frontend pages.

## Project Structure

```text
livecricket/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cricket/
│   │   │           └── livecricket/
│   │   │               ├── client/
│   │   │               │   └── CricketApiClient.java
│   │   │               │
│   │   │               ├── config/
│   │   │               │   └── CorsConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── HealthController.java
│   │   │               │   ├── MatchController.java
│   │   │               │   ├── ExternalMatchController.java
│   │   │               │   └── MatchSyncController.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   └── MatchResponse.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   ├── Match.java
│   │   │               │   └── ScoreSnapshot.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── MatchRepository.java
│   │   │               │   └── ScoreSnapshotRepository.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── CricketApiService.java
│   │   │               │   └── MatchSyncService.java
│   │   │               │
│   │   │               └── LivecricketApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Navbar.jsx
│   │   │   └── MatchCard.jsx
│   │   │
│   │   ├── pages/
│   │   │   ├── Home.jsx
│   │   │   └── MatchDetails.jsx
│   │   │
│   │   ├── services/
│   │   │   └── matchService.js
│   │   │
│   │   ├── App.jsx
│   │   ├── App.css
│   │   └── index.css
│   │
│   ├── index.html
│   ├── package.json
│   ├── package-lock.json
│   └── vite.config.js
│
├── .gitignore
├── application-secret.properties  # Local only; not committed
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Software Requirements

Install the following before running the project:

- Java Development Kit (JDK) 26
- Node.js 24 or compatible version
- npm 11 or compatible version
- MySQL Server and MySQL Workbench
- Visual Studio Code
- Git

## Database Configuration

1. Open MySQL Workbench and connect to your local MySQL server.
2. Create the database:

```sql
CREATE DATABASE live_cricket_db;
```

3. Configure the database connection in `src/main/resources/application.properties`:

```properties
spring.application.name=LiveCricket
server.port=8081

spring.datasource.url=jdbc:mysql://localhost:3307/live_cricket_db
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

spring.config.import=optional:file:./application-secret.properties

cricket.api.base-url=https://api.cricapi.com/v1
cricket.api.refresh-interval=1800000
```

Update the MySQL port, username, and password according to your local configuration.


## CricAPI Configuration

The application uses CricAPI to retrieve external cricket match data.

1. Obtain an API key from your CricAPI account.
2. In the project root, create a file named `application-secret.properties`.
3. Add the following property:

```properties
cricket.api.key=YOUR_ACTUAL_API_KEY
```

Replace `YOUR_ACTUAL_API_KEY` with your own key.

## Installation and Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
cd YOUR_REPOSITORY
```

### 2. Configure the backend

Make sure the MySQL database exists and the database settings and CricAPI key are configured as described above.

### 3. Run the Spring Boot backend

From the project root:

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs at:

```text
http://localhost:8081
```

### 4. Install frontend dependencies

Open a second terminal:

```powershell
cd frontend
npm install
```

### 5. Run the React frontend

```powershell
npm run dev
```

Vite will display a local URL, usually:

```text
http://localhost:5173
```

If that port is already in use, Vite may use another port, such as `5174`. Open the exact URL shown in the terminal.

## API Endpoints

The backend exposes REST endpoints for health checks, match retrieval, external data retrieval, and synchronization.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/health` | Check backend availability |
| GET | `/api/matches` | Retrieve stored matches |
| GET | `/api/matches/live` | Retrieve matches classified as live |
| GET | `/api/matches/{id}` | Retrieve a match by its database ID |
| GET | `/api/external/matches` | Retrieve match data from the external cricket API |
| POST | `/api/matches/sync` | Synchronize match data to the database |
| api/teams | Team management | Manage cricket teams
| api/players | Player management | Manage players and their associated teams

## Testing the Application

1. Start the MySQL server.
2. Start the Spring Boot backend.
3. Open `http://localhost:8081/api/health` to check backend availability.
4. Open `http://localhost:8081/api/matches` to check the stored match endpoint.
5. Trigger synchronization using the POST endpoint.
6. Open /api/teams and /api/players to check the team and player endpoints. 
7. Start the React frontend and open the Vite URL.
8. Check match cards, search, filters, match details, team management, and players. 

## Troubleshooting

### Backend connection issue

- Confirm Spring Boot is running on port `8081`.
- Check that `/api/matches` returns JSON in the browser.
- Check the backend terminal for errors.

### CORS issue

Ensure the backend allows requests from the frontend origin, such as `http://localhost:5173` or `http://localhost:5174`.

### Database connection issue

- Confirm MySQL is running.
- Verify the port, database name, username, and password in `application.properties`.
- Check the Spring Boot terminal for database errors.

### No matches displayed

- Confirm the database has synchronized match records.
- Trigger `/api/matches/sync`.
- Check the API response and backend logs for errors.

### CricAPI errors

- Verify that the API key is valid and correctly configured.
- Check the API account's request limits and the backend logs.


## Screenshots
<img width="1910" height="877" alt="Screenshot 2026-10-03 164232" src="https://github.com/user-attachments/assets/3a572405-5cea-42fe-855a-8ab3faf0a3a2" />
<img width="1917" height="872" alt="Screenshot 2026-10-03 164243" src="https://github.com/user-attachments/assets/32d35184-9d43-4751-84e7-2de6f12dafc5" />
<img width="1917" height="872" alt="Screenshot 2026-10-03 164300" src="https://github.com/user-attachments/assets/49948879-8ca5-4736-b905-21cb8bbc7082" />
<img width="1915" height="870" alt="Screenshot 2026-10-03 164320" src="https://github.com/user-attachments/assets/bb4decf1-97f8-43fd-8f9f-ac2903b5bf87" />
<img width="1917" height="872" alt="Screenshot 2026-10-03 164328" src="https://github.com/user-attachments/assets/cf8188ad-2738-4b9d-ba4f-4ea4ff642eeb" />
<img width="1917" height="867" alt="Screenshot 2026-10-03 164345" src="https://github.com/user-attachments/assets/fc9acd5c-aa15-45a1-aca4-9f7e7ab55690" />
<img width="1917" height="867" alt="Screenshot 2026-10-03 164407" src="https://github.com/user-attachments/assets/8fe5db02-e2aa-482b-a33e-89407fd8ab0e" />
