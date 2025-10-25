<template>
  <div class="space-y-6">
    <!-- Welcome Section -->
    <div>
      <h1 class="text-3xl font-bold text-foreground">Dashboard</h1>
      <p class="text-muted-foreground mt-2">
        Welcome to ZJU Tennis Analysis System
      </p>
    </div>

    <!-- Quick Stats -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <CardHeader>
          <CardTitle class="text-sm font-medium text-muted-foreground">Total Players</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">-</div>
          <p class="text-xs text-muted-foreground mt-1">Active members</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle class="text-sm font-medium text-muted-foreground">Total Matches</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">-</div>
          <p class="text-xs text-muted-foreground mt-1">Recorded this season</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle class="text-sm font-medium text-muted-foreground">Video Analyses</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">-</div>
          <p class="text-xs text-muted-foreground mt-1">AI analyses completed</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle class="text-sm font-medium text-muted-foreground">Upcoming Matches</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">-</div>
          <p class="text-xs text-muted-foreground mt-1">Planned events</p>
        </CardContent>
      </Card>
    </div>

    <!-- Recent Activity & Quick Actions -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Recent Activity -->
      <Card>
        <CardHeader>
          <CardTitle>Recent Activity</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="text-sm text-muted-foreground text-center py-8">
            No recent activity to display
          </div>
        </CardContent>
      </Card>

      <!-- Quick Actions -->
      <Card>
        <CardHeader>
          <CardTitle>Quick Actions</CardTitle>
        </CardHeader>
        <CardContent>
          <div class="space-y-2">
            <Button @click="$router.push('/players/new')" class="w-full justify-start" variant="outline">
              <Plus class="w-4 h-4 mr-2" />
              Add New Player
            </Button>
            <Button @click="$router.push('/matches')" class="w-full justify-start" variant="outline">
              <Trophy class="w-4 h-4 mr-2" />
              Record New Match
            </Button>
            <Button @click="$router.push('/analysis/video')" class="w-full justify-start" variant="outline">
              <Video class="w-4 h-4 mr-2" />
              Upload Video Analysis
            </Button>
            <Button @click="$router.push('/team/planning')" class="w-full justify-start" variant="outline">
              <Calendar class="w-4 h-4 mr-2" />
              Plan Team Lineup
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import Card from '../components/ui/Card.vue';
import CardHeader from '../components/ui/CardHeader.vue';
import CardTitle from '../components/ui/CardTitle.vue';
import CardContent from '../components/ui/CardContent.vue';
import Button from '../components/ui/Button.vue';
import { Plus, Trophy, Video, Calendar } from 'lucide-vue-next';
</script>
