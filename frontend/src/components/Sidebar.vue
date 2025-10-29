<template>
  <aside class="w-64 bg-card border-r border-border flex flex-col h-full">
    <!-- Logo/Brand -->
    <div class="p-6 border-b border-border">
      <router-link to="/" class="flex items-center space-x-2">
        <div class="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
          <span class="text-primary-foreground font-bold text-lg">🎾</span>
        </div>
        <div>
          <h1 class="text-lg font-bold text-foreground">ZJU Tennis</h1>
          <p class="text-xs text-muted-foreground">Analysis System</p>
        </div>
      </router-link>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 overflow-y-auto p-4 space-y-2">
      <!-- Dashboard -->
      <router-link
        to="/dashboard"
        class="nav-item"
        :class="isActive('/dashboard')"
      >
        <LayoutDashboard class="w-5 h-5" />
        <span>Dashboard</span>
      </router-link>

      <!-- Player Management -->
      <div class="space-y-1">
        <div class="nav-section-title">Player Management</div>
        <router-link
          to="/players"
          class="nav-item"
          :class="isActive('/players')"
        >
          <Users class="w-5 h-5" />
          <span>Player List</span>
        </router-link>
      </div>

      <!-- Player Analysis -->
      <div class="space-y-1">
        <div class="nav-section-title">Player Analysis</div>
        <router-link
          to="/analysis/evaluation"
          class="nav-item"
          :class="isActive('/analysis/evaluation')"
        >
          <MessageSquare class="w-5 h-5" />
          <span>Player Evaluation</span>
        </router-link>
        <router-link
          to="/analysis/growth"
          class="nav-item"
          :class="isActive('/analysis/growth')"
        >
          <TrendingUp class="w-5 h-5" />
          <span>Growth Trajectory</span>
        </router-link>
        <router-link
          to="/analysis/singles-doubles"
          class="nav-item"
          :class="isActive('/analysis/singles-doubles')"
        >
          <Users2 class="w-5 h-5" />
          <span>Singles/Doubles</span>
        </router-link>
        <router-link
          to="/analysis/prediction"
          class="nav-item"
          :class="isActive('/analysis/prediction')"
        >
          <Brain class="w-5 h-5" />
          <span>Win Prediction</span>
        </router-link>
        <router-link
          to="/analysis/video"
          class="nav-item"
          :class="isActive('/analysis/video')"
        >
          <Video class="w-5 h-5" />
          <span>Video Analysis</span>
        </router-link>
      </div>

      <!-- Match Analysis -->
      <div class="space-y-1">
        <div class="nav-section-title">Match Analysis</div>
        <router-link
          to="/matches"
          class="nav-item"
          :class="isActive('/matches')"
        >
          <Trophy class="w-5 h-5" />
          <span>Match Records</span>
        </router-link>
        <router-link
          to="/matches/summary"
          class="nav-item"
          :class="isActive('/matches/summary')"
        >
          <FileText class="w-5 h-5" />
          <span>Match Summary</span>
        </router-link>
      </div>

      <!-- Team Composition -->
      <div class="space-y-1">
        <div class="nav-section-title">Team Composition</div>
        <router-link
          to="/team/planning"
          class="nav-item"
          :class="isActive('/team/planning')"
        >
          <Calendar class="w-5 h-5" />
          <span>Match Planning</span>
        </router-link>
        <router-link
          to="/team/lineup"
          class="nav-item"
          :class="isActive('/team/lineup')"
        >
          <GitBranch class="w-5 h-5" />
          <span>Lineup Optimization</span>
        </router-link>
      </div>
    </nav>

    <!-- Footer/User Section -->
    <div class="p-4 border-t border-border">
      <div class="text-xs text-muted-foreground">
        <p>v1.0.0</p>
        <p>© 2025 ZJU Tennis</p>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { useRoute } from 'vue-router';
import {
  LayoutDashboard,
  Users,
  MessageSquare,
  TrendingUp,
  Users2,
  Brain,
  Video,
  Trophy,
  FileText,
  Calendar,
  GitBranch
} from 'lucide-vue-next';

const route = useRoute();

const isActive = (path) => {
  return route.path.startsWith(path)
    ? 'bg-accent text-accent-foreground'
    : 'text-muted-foreground hover:bg-accent/50 hover:text-foreground';
};
</script>

<style scoped>
.nav-item {
  @apply flex items-center space-x-3 px-3 py-2 rounded-md text-sm font-medium transition-colors cursor-pointer;
}

.nav-section-title {
  @apply px-3 py-2 text-xs font-semibold text-muted-foreground uppercase tracking-wider;
}
</style>
