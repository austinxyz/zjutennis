<template>
  <div v-if="hasPerformanceMetrics" class="space-y-4">
    <h4 class="text-sm font-semibold">Performance Metrics</h4>
    <div class="space-y-3">
      <!-- Serve Percentage -->
      <div v-if="statistics.servePercentage !== null && statistics.servePercentage !== undefined">
        <div class="flex justify-between text-sm mb-1">
          <span class="text-muted-foreground">Serve %</span>
          <span class="font-medium">{{ statistics.servePercentage.toFixed(1) }}%</span>
        </div>
        <div class="h-2 bg-muted rounded-full overflow-hidden">
          <div
            class="h-full bg-blue-500 transition-all"
            :style="{ width: `${statistics.servePercentage}%` }"
          ></div>
        </div>
      </div>

      <!-- First Serve Percentage -->
      <div v-if="statistics.firstServePercentage !== null && statistics.firstServePercentage !== undefined">
        <div class="flex justify-between text-sm mb-1">
          <span class="text-muted-foreground">1st Serve %</span>
          <span class="font-medium">{{ statistics.firstServePercentage.toFixed(1) }}%</span>
        </div>
        <div class="h-2 bg-muted rounded-full overflow-hidden">
          <div
            class="h-full bg-blue-500 transition-all"
            :style="{ width: `${statistics.firstServePercentage}%` }"
          ></div>
        </div>
      </div>

      <!-- Break Point Conversion -->
      <div v-if="statistics.breakPointConversion !== null && statistics.breakPointConversion !== undefined">
        <div class="flex justify-between text-sm mb-1">
          <span class="text-muted-foreground">Break Point Conversion</span>
          <span class="font-medium">{{ statistics.breakPointConversion.toFixed(1) }}%</span>
        </div>
        <div class="h-2 bg-muted rounded-full overflow-hidden">
          <div
            class="h-full bg-blue-500 transition-all"
            :style="{ width: `${statistics.breakPointConversion}%` }"
          ></div>
        </div>
      </div>
    </div>

    <!-- Additional Stats Grid -->
    <div v-if="hasAdditionalStats" class="grid grid-cols-2 gap-3 pt-2 mt-2 border-t">
      <div v-if="statistics.singlesWinRate !== null && statistics.singlesWinRate !== undefined" class="text-center">
        <div class="text-xs text-muted-foreground">Singles Win Rate</div>
        <div class="text-lg font-bold text-blue-600">{{ statistics.singlesWinRate.toFixed(1) }}%</div>
      </div>

      <div v-if="statistics.doublesWinRate !== null && statistics.doublesWinRate !== undefined" class="text-center">
        <div class="text-xs text-muted-foreground">Doubles Win Rate</div>
        <div class="text-lg font-bold text-green-600">{{ statistics.doublesWinRate.toFixed(1) }}%</div>
      </div>

      <div v-if="statistics.matchesPerMonth !== null && statistics.matchesPerMonth !== undefined" class="text-center">
        <div class="text-xs text-muted-foreground">Matches/Month</div>
        <div class="text-lg font-bold">{{ statistics.matchesPerMonth }}</div>
      </div>

      <div v-if="statistics.practiceHoursPerWeek !== null && statistics.practiceHoursPerWeek !== undefined" class="text-center">
        <div class="text-xs text-muted-foreground">Practice hrs/Week</div>
        <div class="text-lg font-bold">{{ statistics.practiceHoursPerWeek }}</div>
      </div>

      <div v-if="statistics.averageMatchDurationMinutes" class="text-center">
        <div class="text-xs text-muted-foreground">Avg Match Duration</div>
        <div class="text-lg font-bold">{{ Math.floor(statistics.averageMatchDurationMinutes / 60) }}h {{ statistics.averageMatchDurationMinutes % 60 }}m</div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  statistics: {
    type: Object,
    default: () => ({})
  }
});

const hasPerformanceMetrics = computed(() => {
  const s = props.statistics || {};
  return (s.servePercentage !== null && s.servePercentage !== undefined) ||
         (s.firstServePercentage !== null && s.firstServePercentage !== undefined) ||
         (s.breakPointConversion !== null && s.breakPointConversion !== undefined);
});

const hasAdditionalStats = computed(() => {
  const s = props.statistics || {};
  return (s.singlesWinRate !== null && s.singlesWinRate !== undefined) ||
         (s.doublesWinRate !== null && s.doublesWinRate !== undefined) ||
         (s.matchesPerMonth !== null && s.matchesPerMonth !== undefined) ||
         (s.practiceHoursPerWeek !== null && s.practiceHoursPerWeek !== undefined) ||
         s.averageMatchDurationMinutes;
});
</script>
