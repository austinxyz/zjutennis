<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-foreground">Growth Trajectory</h1>
      <p class="text-muted-foreground mt-2">
        Track player development and performance trends over time
      </p>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <TrendingUp class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Growth Trajectory Analysis</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Visualize player development with comprehensive growth analytics and
            AI-generated insights.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• Calculate age, playing years, and career start date</p>
            <p>• Display growth curves (UTR changes, win rate, skill trends)</p>
            <p>• Auto-generate growth reports with AI text summaries</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import { TrendingUp } from 'lucide-vue-next';
</script>
