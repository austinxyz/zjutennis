# ZJU Tennis Frontend

Vue 3 + Vite + Tailwind CSS frontend for ZJU Tennis Player Analysis System

## Setup

```bash
cd frontend
npm install
```

## Development

```bash
npm run dev
```

The app will run on http://localhost:3000 and proxy API requests to http://localhost:8080

## Build

```bash
npm run build
```

## Features

- **Player List**: View all players with filtering and sorting
- **Player Edit**: Edit player information with three tabs:
  - **Basic Information**: Name, email, contact info, ratings
  - **Skills**: Technical skills evaluation (1-10 scale)
  - **Statistics**: Match stats, activity level, preferences

## Technology Stack

- Vue 3 - Progressive JavaScript framework
- Vue Router - Official router for Vue.js
- Vite - Next generation frontend tooling
- Tailwind CSS - Utility-first CSS framework
- Axios - Promise based HTTP client
