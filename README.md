# ZJU Tennis - Player Analysis System

A full-stack web application for managing and analyzing ZJU alumni tennis players, featuring comprehensive player profiles, skills assessment, and statistical tracking.

## Features

- **Player Management**: CRUD operations for player profiles with detailed information
- **Skills Assessment**: Track and rate player technical skills (forehand, backhand, serve, etc.)
- **Statistics Tracking**: Monitor player ratings (UTR, NTRP, Dynamic Rating) and performance metrics
- **UTR Update Tracking**: Automatically tracks when player's UTR rating is updated with timestamp
- **Alumni Information**: Support for multiple graduation universities for player and their couple (up to 3 each)
- **Sortable Player List**: View and sort players by UTR, NTRP, or gender
- **Tabbed Edit Interface**: Organized player data entry with separate tabs for basic info, alumni, skills, and statistics
- **CSV Import/Export**: Bulk import player data from CSV files and export selected players
- **Database Migrations**: Organized migration scripts with comprehensive documentation
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
│   │   │   │   ├── PlayerAlumni.java
│   │   │   │   ├── PlayerSkills.java
│   │   │   │   └── PlayerStatistics.java
│   │   │   ├── repository/
│   │   │   │   ├── PlayerRepository.java
│   │   │   │   ├── PlayerAlumniRepository.java
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
│   │   │   ├── PlayerAlumni.vue
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
├── db/                          # Database migration scripts
│   ├── README.md                # Migration documentation
│   ├── setup_database.sql
│   ├── create_skill_tables.sql
│   ├── add_gender_field.sql
│   ├── add_rating_status_fields.sql
│   ├── add_statistics_url_fields.sql
│   ├── db_migration_add_utr_updated_date.sql
│   └── db_migration_add_player_alumni.sql
└── pom.xml
```

## Database Schema

### Player
- Basic information: name, email, gender, phone number
- Location: city, country
- Relationships: OneToOne with PlayerSkills, PlayerStatistics, and PlayerAlumni

### PlayerAlumni
- Player's graduation universities: up to 3 universities with graduation years
- Couple's graduation universities: up to 3 universities with graduation years
- Automatically managed with cascade operations
- Created/updated timestamps

### PlayerSkills
- Technical ratings (1-10): forehand, backhand, serve, volley, smash, etc.
- Mental & physical: mental strength, movement, fitness
- Tactical: court positioning, shot selection, competitive spirit
- Notes: strengths, weaknesses, general notes

### PlayerStatistics
- Ratings: UTR, NTRP, Dynamic Rating, Self Rating
- UTR tracking: rating, status, URL, and last updated timestamp
- NTRP tracking: rating, status, URL
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

### Database Migrations

All database migration scripts are located in the `db/` folder with comprehensive documentation:

```bash
db/
├── README.md                                    # Complete migration documentation
├── setup_database.sql                           # Initial database and tables setup
├── create_skill_tables.sql                      # Player skills and statistics tables
├── add_gender_field.sql                         # Add gender field to players
├── add_rating_status_fields.sql                 # Add UTR/NTRP status fields
├── add_statistics_url_fields.sql                # Add URL fields for ratings
├── remove_duplicate_fields_from_players.sql     # Remove duplicate UTR fields
├── db_migration_add_utr_updated_date.sql        # Add UTR update tracking
└── db_migration_add_player_alumni.sql           # Add alumni information table
```

To run migrations, see the detailed instructions in `db/README.md`. Migration order is important - follow the documentation carefully.

**Quick migration example:**
```bash
mysql -h your-host -P port -u username -p database_name < db/db_migration_add_utr_updated_date.sql
```

**Important:** Always restart the backend server after running database migrations to ensure schema changes are picked up.

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

Players are returned with embedded skills, statistics, and alumni information:

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "gender": "male",
  "city": "San Francisco",
  "country": "USA",
  "phoneNumber": "+1234567890",
  "alumni": {
    "graduationUniversity1": "Zhejiang University",
    "graduationYear1": 2020,
    "graduationUniversity2": "Stanford University",
    "graduationYear2": 2022,
    "coupleGraduationUniversity1": "UC Berkeley",
    "coupleGraduationYear1": 2019
  },
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
    "utrUrl": "https://app.myutr.com/profiles/12345",
    "utrUpdatedDate": "2025-10-23T14:30:00",
    "ntrpRating": 4.5,
    "ntrpStatus": "verified",
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
