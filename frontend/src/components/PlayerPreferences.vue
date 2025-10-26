<template>
  <div v-if="hasPreferences" class="space-y-4">
    <h3 class="font-semibold text-sm">Playing Preferences</h3>

    <div class="space-y-3">
      <!-- Preferred Surface -->
      <div v-if="statistics.preferredSurface" class="flex items-center gap-3">
        <div class="flex-shrink-0 w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
          <span class="text-xs font-semibold text-blue-700">⛳</span>
        </div>
        <div class="flex-1">
          <div class="text-xs text-muted-foreground">Surface</div>
          <div class="text-sm font-medium capitalize">{{ statistics.preferredSurface }}</div>
        </div>
      </div>

      <!-- Preferred Playing Style -->
      <div v-if="statistics.preferredPlayingStyle" class="flex items-center gap-3">
        <div class="flex-shrink-0 w-8 h-8 rounded-full bg-green-100 flex items-center justify-center">
          <span class="text-xs font-semibold text-green-700">🎾</span>
        </div>
        <div class="flex-1">
          <div class="text-xs text-muted-foreground">Playing Style</div>
          <div class="text-sm font-medium capitalize">{{ formatStyle(statistics.preferredPlayingStyle) }}</div>
        </div>
      </div>

      <!-- Dominant Hand -->
      <div v-if="statistics.dominantHand" class="flex items-center gap-3">
        <div class="flex-shrink-0 w-8 h-8 rounded-full bg-purple-100 flex items-center justify-center">
          <span class="text-xs font-semibold text-purple-700">✋</span>
        </div>
        <div class="flex-1">
          <div class="text-xs text-muted-foreground">Dominant Hand</div>
          <div class="text-sm font-medium capitalize">{{ statistics.dominantHand }}</div>
        </div>
      </div>

      <!-- Preferred Doubles Position -->
      <div v-if="statistics.preferredDoublesPosition" class="flex items-center gap-3">
        <div class="flex-shrink-0 w-8 h-8 rounded-full bg-orange-100 flex items-center justify-center">
          <span class="text-xs font-semibold text-orange-700">👥</span>
        </div>
        <div class="flex-1">
          <div class="text-xs text-muted-foreground">Doubles Position</div>
          <div class="text-sm font-medium capitalize">{{ formatStyle(statistics.preferredDoublesPosition) }}</div>
        </div>
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

const hasPreferences = computed(() => {
  const s = props.statistics || {};
  return s.preferredSurface || s.preferredPlayingStyle || s.dominantHand || s.preferredDoublesPosition;
});

const formatStyle = (value) => {
  if (!value) return '';
  return value.replace(/-/g, ' ');
};
</script>
