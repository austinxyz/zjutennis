# Development Tools Configuration

This document describes the tool configuration setup for the ZJU Tennis project.

## Overview

The project uses a centralized configuration file (`.tool-config.json`) to store paths to development tools. This ensures consistent tool usage across different environments and makes it easy to reference tool paths.

## Installed Tools

The following tools are configured:

- **Maven 3.9.11** - Java build tool
- **OpenJDK 25** - Java Development Kit
- **MySQL Client 9.5.0** - Database client
- **GitHub CLI 2.82.1** - GitHub command-line tool

## Configuration Files

### 1. `.tool-config.json`

Main configuration file containing all tool paths and environment variables.

**Location:** `/Users/yanzxu/claude/zjutennis/.tool-config.json`

**Structure:**
```json
{
  "tools": {
    "maven": { "path": "...", "home": "...", "version": "..." },
    "java": { "path": "...", "javac_path": "...", "home": "...", "version": "..." },
    "mysql": { "path": "...", "client_home": "..." },
    "github": { "path": "..." }
  },
  "environment": {
    "JAVA_HOME": "...",
    "MAVEN_HOME": "..."
  }
}
```

### 2. Helper Scripts

#### Bash Script: `scripts/load-tools.sh`

Loads tool paths into environment variables.

**Usage:**
```bash
# Source the script to load environment variables
source scripts/load-tools.sh

# Then use the tools
$MAVEN_BIN clean install
$JAVA_BIN -version
$MYSQL_BIN -u root -p
$GH_BIN repo list
```

**Environment Variables Set:**
- `MAVEN_BIN` - Maven executable path
- `MAVEN_HOME` - Maven home directory
- `JAVA_BIN` - Java executable path
- `JAVA_HOME` - Java home directory
- `MYSQL_BIN` - MySQL client executable path
- `GH_BIN` - GitHub CLI executable path

#### Python Script: `scripts/tool_config.py`

Provides programmatic access to tool configuration.

**Usage:**
```python
from scripts.tool_config import ToolConfig

config = ToolConfig()

# Access tool paths
maven_path = config.maven
java_path = config.java
mysql_path = config.mysql
gh_path = config.github_cli

# Access environment variables
java_home = config.java_home
maven_home = config.maven_home

# Print full configuration
config.print_config()
```

**Standalone Usage:**
```bash
python3 scripts/tool_config.py
```

## Common Tasks

### Build the Backend with Maven

```bash
# Using absolute path from config
/opt/homebrew/bin/mvn clean package

# Or with environment variables
source scripts/load-tools.sh
$MAVEN_BIN clean package
```

### Run Java Application

```bash
# Using absolute path from config
/opt/homebrew/Cellar/openjdk/25/bin/java -jar target/app.jar

# Or with environment variables
source scripts/load-tools.sh
$JAVA_BIN -jar target/app.jar
```

### Connect to MySQL Database

```bash
# Using absolute path from config
/opt/homebrew/opt/mysql-client/bin/mysql -h localhost -u root -p zjutennis

# Or with environment variables
source scripts/load-tools.sh
$MYSQL_BIN -h localhost -u root -p zjutennis
```

### Use GitHub CLI

```bash
# Using absolute path from config
/opt/homebrew/bin/gh repo view

# Or with environment variables
source scripts/load-tools.sh
$GH_BIN repo view
```

## Tool Versions

| Tool | Version | Installation Method |
|------|---------|-------------------|
| Maven | 3.9.11 | Homebrew |
| OpenJDK | 25 | Homebrew |
| MySQL Client | 9.5.0 | Homebrew |
| GitHub CLI | 2.82.1 | Homebrew |

## Platform Information

- **OS:** macOS 15.7.1 (Apple Silicon)
- **Architecture:** ARM64 (aarch64)
- **Package Manager:** Homebrew
- **Homebrew Prefix:** `/opt/homebrew`

## Updating the Configuration

If you upgrade any tools, update the `.tool-config.json` file with new paths/versions:

```bash
# Find new paths
which mvn
which java
which mysql
which gh

# Update .tool-config.json manually or regenerate
```

## Notes

- All tools are installed via Homebrew for consistent package management
- MySQL Server is not installed, only the MySQL client
- MySQL Workbench is installed separately for GUI database management
- The configuration assumes tools remain in their Homebrew-managed locations

## Troubleshooting

### Java not found on PATH

Run:
```bash
source scripts/load-tools.sh
```

This adds all tool directories to your PATH.

### MySQL connection issues

Ensure MySQL server is running:
```bash
# If you have MySQL server installed
brew services start mysql

# Or use Docker
docker-compose up -d mysql
```

### Permission denied on scripts

Make scripts executable:
```bash
chmod +x scripts/load-tools.sh
chmod +x scripts/tool_config.py
```
