#!/usr/bin/env python3
"""
Tool Configuration Helper for ZJU Tennis Project
Loads tool paths from .tool-config.json
"""

import json
import os
from pathlib import Path

class ToolConfig:
    def __init__(self):
        self.project_root = Path(__file__).parent.parent
        self.config_file = self.project_root / '.tool-config.json'
        self.config = self._load_config()

    def _load_config(self):
        """Load configuration from JSON file"""
        if not self.config_file.exists():
            raise FileNotFoundError(f"Configuration file not found: {self.config_file}")

        with open(self.config_file, 'r') as f:
            return json.load(f)

    def get_tool_path(self, tool_name):
        """Get the path for a specific tool"""
        if tool_name not in self.config['tools']:
            raise ValueError(f"Tool '{tool_name}' not found in configuration")
        return self.config['tools'][tool_name]['path']

    def get_env_var(self, var_name):
        """Get an environment variable from configuration"""
        if var_name not in self.config['environment']:
            raise ValueError(f"Environment variable '{var_name}' not found in configuration")
        return self.config['environment'][var_name]

    @property
    def maven(self):
        """Get Maven executable path"""
        return self.config['tools']['maven']['path']

    @property
    def java(self):
        """Get Java executable path"""
        return self.config['tools']['java']['path']

    @property
    def javac(self):
        """Get Javac executable path"""
        return self.config['tools']['java']['javac_path']

    @property
    def mysql(self):
        """Get MySQL client executable path"""
        return self.config['tools']['mysql']['path']

    @property
    def github_cli(self):
        """Get GitHub CLI executable path"""
        return self.config['tools']['github']['path']

    @property
    def java_home(self):
        """Get JAVA_HOME path"""
        return self.config['environment']['JAVA_HOME']

    @property
    def maven_home(self):
        """Get MAVEN_HOME path"""
        return self.config['environment']['MAVEN_HOME']

    def print_config(self):
        """Print all configuration in a readable format"""
        print("=== ZJU Tennis Tool Configuration ===")
        print(f"\nProject Root: {self.project_root}")
        print(f"Config File: {self.config_file}")
        print("\nTools:")
        for tool_name, tool_info in self.config['tools'].items():
            print(f"  {tool_name.upper()}: {tool_info['path']}")
        print("\nEnvironment Variables:")
        for var_name, var_value in self.config['environment'].items():
            print(f"  {var_name}: {var_value}")
        print()

# Example usage
if __name__ == '__main__':
    config = ToolConfig()
    config.print_config()

    # Example: Run Maven with configured path
    print("Example commands:")
    print(f"  Maven build: {config.maven} clean package")
    print(f"  Java run: {config.java} -version")
    print(f"  MySQL connect: {config.mysql} -u root -p")
    print(f"  GitHub CLI: {config.github_cli} repo view")
