# Database Migrations

This folder contains SQL migration scripts for the ZJU Tennis Player Analysis System.

## Migration Scripts

### 1. db_migration_add_utr_updated_date.sql
**Date:** 2025-10-23
**Purpose:** Add UTR updated date tracking to player statistics

**Changes:**
- Adds `utr_updated_date` column to `player_statistics` table
- Column type: DATETIME NULL
- Automatically updated by backend when UTR rating changes

**How to run:**
```bash
mysql -h 10.0.0.7 -P 37719 -u austinxu -phelloworld zjualumni < db/db_migration_add_utr_updated_date.sql
```

---

### 2. db_migration_add_player_alumni.sql
**Date:** 2025-10-23
**Purpose:** Add alumni information table and migrate existing data

**Changes:**
- Creates `player_alumni` table with:
  - 3 graduation universities with years for the player
  - 3 graduation universities with years for the player's couple
  - Timestamps (created_at, updated_at)
- Migrates existing `graduation_year` data from `players` table
- Removes `graduation_year` and `major` columns from `players` table

**How to run:**
```bash
mysql -h 10.0.0.7 -P 37719 -u austinxu -phelloworld zjualumni < db/db_migration_add_player_alumni.sql
```

---

### 3. remove_duplicate_fields_from_players.sql
**Date:** 2025-10-22
**Purpose:** Remove duplicate UTR rating and status fields from players table

**Changes:**
- Removes `utr_rating` column from `players` table
- Removes `status` column from `players` table
- These fields are now only maintained in `player_statistics` table
- Reduces data duplication and ensures single source of truth

**How to run:**
```bash
mysql -h 10.0.0.7 -P 37719 -u austinxu -phelloworld zjualumni < db/remove_duplicate_fields_from_players.sql
```

**Prerequisites:**
- Ensure `player_statistics` table exists and contains all UTR data
- Run this AFTER `create_skill_tables.sql`

---

## Migration Order

**IMPORTANT:** Run migrations in this order:

1. `setup_database.sql` (initial setup)
2. `create_skill_tables.sql` (creates player_skills and player_statistics tables)
3. `add_gender_field.sql`
4. `add_rating_status_fields.sql`
5. `add_statistics_url_fields.sql`
6. `remove_duplicate_fields_from_players.sql`
7. `db_migration_add_utr_updated_date.sql`
8. `db_migration_add_player_alumni.sql`

## Database Connection

- **Host:** 10.0.0.7
- **Port:** 37719
- **Database:** zjualumni
- **Username:** austinxu (or as configured in .env)
- **Password:** (as configured in .env)

## Running Migrations

### Using MySQL Workbench:
1. Open MySQL Workbench
2. Connect to the database
3. Open the SQL file: File → Open SQL Script
4. Execute the script: Query → Execute (or click the lightning bolt icon)

### Using MySQL Command Line:
```bash
mysql -h 10.0.0.7 -P 37719 -u austinxu -p zjualumni < db/migration_file.sql
```

## After Running Migrations

After running database migrations, always:
1. Restart the backend server to pick up schema changes
2. Test the affected features in the UI
3. Verify data integrity

## Rollback

These migrations are destructive (they drop columns). Before running:
1. Backup your database
2. Test on a development environment first
3. Ensure you have a rollback plan if needed

## Notes

- All migrations have been tested and executed successfully on production
- The backend application expects these schema changes
- Do not modify these files after they have been executed
