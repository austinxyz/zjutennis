<template>
  <div class="flex flex-col min-h-screen bg-background">
    <header class="bg-card border-b border-border shadow-sm">
      <div class="container mx-auto px-4">
        <div class="flex h-16 items-center justify-between">
          <div class="flex items-center">
            <router-link to="/" class="text-2xl font-bold text-primary">
              ZJU Tennis Analysis
            </router-link>
          </div>

          <nav class="flex items-center space-x-6">
            <router-link
              to="/players"
              class="px-3 py-2 text-sm font-medium rounded-md hover:bg-accent hover:text-accent-foreground transition-colors"
              :class="$route.path.startsWith('/players') ? 'bg-accent text-accent-foreground' : 'text-foreground'"
            >
              Players
            </router-link>
          </nav>
        </div>
      </div>
    </header>

    <main class="flex-1">
      <RouterView />
    </main>

    <footer class="bg-card border-t border-border py-6">
      <div class="container mx-auto px-4 text-center text-sm text-muted-foreground">
        &copy; 2025 ZJU Tennis Analysis System
      </div>
    </footer>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router';
</script>
