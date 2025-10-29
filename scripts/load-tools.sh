#!/bin/bash
# Load tool configurations from .tool-config.json
# Usage: source scripts/load-tools.sh

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
CONFIG_FILE="$PROJECT_ROOT/.tool-config.json"

if [ ! -f "$CONFIG_FILE" ]; then
    echo "Error: Configuration file not found at $CONFIG_FILE"
    return 1
fi

# Extract paths using grep and sed (works without jq)
export MAVEN_BIN=$(grep -A 1 '"maven"' "$CONFIG_FILE" | grep '"path"' | sed 's/.*: "\(.*\)".*/\1/')
export MAVEN_HOME=$(grep -A 2 '"maven"' "$CONFIG_FILE" | grep '"home"' | sed 's/.*: "\(.*\)".*/\1/')
export JAVA_BIN=$(grep -A 1 '"java"' "$CONFIG_FILE" | grep '"path"' | sed 's/.*: "\(.*\)".*/\1/')
export JAVA_HOME=$(grep -A 3 '"java"' "$CONFIG_FILE" | grep '"home"' | sed 's/.*: "\(.*\)".*/\1/')
export MYSQL_BIN=$(grep -A 1 '"mysql"' "$CONFIG_FILE" | grep '"path"' | sed 's/.*: "\(.*\)".*/\1/')
export GH_BIN=$(grep -A 1 '"github"' "$CONFIG_FILE" | grep '"path"' | sed 's/.*: "\(.*\)".*/\1/')

# Add to PATH
export PATH="$(dirname $JAVA_BIN):$(dirname $MAVEN_BIN):$(dirname $MYSQL_BIN):$(dirname $GH_BIN):$PATH"

echo "✓ Tool paths loaded from $CONFIG_FILE"
echo "  Maven: $MAVEN_BIN"
echo "  Java: $JAVA_BIN"
echo "  MySQL: $MYSQL_BIN"
echo "  GitHub CLI: $GH_BIN"
echo ""
echo "  JAVA_HOME: $JAVA_HOME"
echo "  MAVEN_HOME: $MAVEN_HOME"
