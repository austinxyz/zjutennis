# ZJU Tennis - Player Analysis System

A full-stack web application for managing and analyzing ZJU alumni tennis players, featuring comprehensive player profiles, skills assessment, and statistical tracking.

## Features

- **Player Management**: CRUD operations for player profiles with detailed information
- **Skills Assessment**: Track and rate player technical skills (forehand, backhand, serve, etc.)
- **Statistics Tracking**: Monitor player ratings (UTR, NTRP, Dynamic Rating) and performance metrics
- **Sortable Player List**: View and sort players by UTR, NTRP, or gender
- **Tabbed Edit Interface**: Organized player data entry with separate tabs for basic info, skills, and statistics
- **CSV Import/Export**: Bulk import player data from CSV files and export selected players
- **Modern UI**: Professional design with shadcn-vue component library
- **Responsive Design**: Mobile-friendly interface with Tailwind CSS and theme support
- **Accessible Components**: WCAG-compliant UI components built on Radix Vue primitives

## Technology Stack

### Backend
- Java 17
- Spring Boot 2.7.5
- Spring Data JPA
- MySQL 8
- Maven
- Lombok

### Frontend
- Vue 3 (Composition API)
- Vue Router 4
- Axios
- Tailwind CSS
- shadcn-vue (UI Component Library)
- Radix Vue (Headless UI Components)
- Lucide Vue Next (Icons)
- Class Variance Authority (Component Variants)
- Vite

## Project Structure

```
zjutennis/
├── src/
│   ├── main/
│   │   ├── java/com/zjutennis/
│   │   │   ├── ZjutennisApplication.java
│   │   │   ├── config/
│   │   │   │   └── DataLoader.java
│   │   │   ├── controller/
│   │   │   │   └── PlayerController.java
│   │   │   ├── dto/
│   │   │   │   └── ImportResult.java
│   │   │   ├── model/
│   │   │   │   ├── Player.java
│   │   │   │   ├── PlayerSkills.java
│   │   │   │   └── PlayerStatistics.java
│   │   │   ├── repository/
│   │   │   │   ├── PlayerRepository.java
│   │   │   │   ├── PlayerSkillsRepository.java
│   │   │   │   └── PlayerStatisticsRepository.java
│   │   │   ├── service/
│   │   │   │   ├── PlayerService.java
│   │   │   │   ├── PlayerSkillsService.java
│   │   │   │   ├── PlayerStatisticsService.java
│   │   │   │   └── DataImportService.java
│   │   │   └── util/
│   │   │       └── CSVUtil.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ZJUAlumni.csv
│   └── test/
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── ui/              # shadcn-vue UI components
│   │   │   │   ├── Button.vue
│   │   │   │   ├── Card.vue
│   │   │   │   ├── Input.vue
│   │   │   │   ├── Label.vue
│   │   │   │   ├── Textarea.vue
│   │   │   │   ├── Badge.vue
│   │   │   │   ├── Table.vue
│   │   │   │   └── ... (other UI components)
│   │   │   ├── PlayerBasicInfo.vue
│   │   │   ├── PlayerSkills.vue
│   │   │   └── PlayerStatistics.vue
│   │   ├── views/
│   │   │   ├── PlayerList.vue
│   │   │   └── PlayerEdit.vue
│   │   ├── services/
│   │   │   └── playerService.js
│   │   ├── router/
│   │   │   └── index.js
│   │   ├── lib/
│   │   │   └── utils.js         # Utility functions (cn helper)
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── tailwind.config.cjs
└── pom.xml
```

## Database Schema

### Player
- Basic information: name, email, gender, phone number
- Location: city, country
- Academic: graduation year, major
- Relationships: OneToOne with PlayerSkills and PlayerStatistics

### PlayerSkills
- Technical ratings (1-10): forehand, backhand, serve, volley, smash, etc.
- Mental & physical: mental strength, movement, fitness
- Tactical: court positioning, shot selection, competitive spirit
- Notes: strengths, weaknesses, general notes

### PlayerStatistics
- Ratings: UTR, NTRP, Dynamic Rating, Self Rating
- Match statistics: total matches, wins, losses, win rates
- Performance: serve percentage, break point conversion
- Preferences: surface, playing style, doubles position
- Activity: play frequency, practice hours, tournament participation

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+ and npm

### Database Configuration

1. Create a `.env` file in the project root (copy from `.env.example`):

```bash
cp .env.example .env
```

2. Update the `.env` file with your database credentials:

```properties
DB_URL=jdbc:mysql://your-host:port/zjualumni?ssl-mode=REQUIRED
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

The application will automatically load these environment variables. The `.env` file is gitignored for security.

Alternatively, you can set environment variables directly:

```bash
export DB_URL=jdbc:mysql://your-host:port/zjualumni?ssl-mode=REQUIRED
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

3. Create the database schema:

```sql
CREATE DATABASE zjualumni;
```

The application will auto-create tables on first run (`spring.jpa.hibernate.ddl-auto=none` is set, but entities are configured with proper JPA annotations).

### UI Components

The frontend uses **shadcn-vue**, a modern component library that provides:

- **Customizable Components**: Built on top of Radix Vue primitives for full control
- **Accessible by Default**: WCAG-compliant components with proper ARIA attributes
- **Beautiful Design**: Professional styling with Tailwind CSS
- **Type-safe**: Written in TypeScript-compatible patterns
- **Icon System**: Lucide Vue Next for consistent, modern icons

**Key Components Used:**
- `Button` - Multiple variants (default, outline, ghost, destructive)
- `Card` - Container components with header, title, and content sections
- `Table` - Fully-featured data tables with sorting capabilities
- `Input` - Form inputs with consistent styling
- `Label` - Accessible form labels
- `Textarea` - Multi-line text inputs
- `Badge` - Status and category indicators

All components follow a consistent design system with proper spacing, typography, and color schemes.

### Backend Setup

1. Build the project:

```bash
./mvnw clean install
```

2. Run the application:

```bash
# If using .env file
set -a && source .env && set +a && ./mvnw spring-boot:run

# Or if environment variables are already set
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Run the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## API Endpoints

### Player Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/players` | Get all players |
| GET | `/api/players/{id}` | Get player by ID |
| POST | `/api/players` | Create new player |
| PUT | `/api/players/{id}` | Update player |
| DELETE | `/api/players/{id}` | Delete player |
| POST | `/api/players/import` | Import players from CSV |

### Response Format

Players are returned with embedded skills and statistics:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "gender": "Male",
  "graduationYear": 2020,
  "major": "Computer Science",
  "city": "San Francisco",
  "country": "USA",
  "phoneNumber": "+1234567890",
  "skills": {
    "forehand": 8,
    "backhand": 7,
    "serve": 9,
    "strengths": "Powerful serve, consistent baseline play",
    "weaknesses": "Net play needs improvement"
  },
  "statistics": {
    "utrRating": 10.5,
    "utrStatus": "verified",
    "ntrpRating": 4.5,
    "totalMatches": 50,
    "wins": 35,
    "losses": 15,
    "winRate": 70.0
  }
}
```

## Data Import

The application supports CSV import with automatic data loading on startup:

1. Place your CSV file in `src/main/resources/`
2. Configure the file path in `DataLoader.java`
3. On startup, if no players exist, the CSV will be automatically imported
4. CSV format: name, email, city, utrRating, graduationYear, status, major, country

## Development

### Running Tests

```bash
./mvnw test
```

### Building for Production

Backend:
```bash
./mvnw clean package
java -jar target/zjutennis-0.0.1-SNAPSHOT.jar
```

Frontend:
```bash
cd frontend
npm run build
```

The production build will be in `frontend/dist/`

## Security Notes

- Database credentials are managed via environment variables
- `.env` file is excluded from version control
- Never commit sensitive credentials to the repository
- Use `.env.example` as a template for other developers

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is private and proprietary.

## Contact

For questions or support, please contact the development team.
