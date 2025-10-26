<template>
  <div v-if="hasData" class="space-y-4">
    <h3 class="font-semibold text-sm">Goals & Availability</h3>

    <!-- Goals -->
    <div v-if="statistics.goals" class="space-y-2">
      <div class="text-xs font-medium text-muted-foreground">Goals</div>
      <div class="text-sm whitespace-pre-wrap bg-muted/30 p-3 rounded-md">{{ statistics.goals }}</div>
    </div>

    <!-- Availability -->
    <div v-if="statistics.availability" class="space-y-2">
      <div class="text-xs font-medium text-muted-foreground">Availability</div>
      <div class="text-sm whitespace-pre-wrap bg-muted/30 p-3 rounded-md">{{ statistics.availability }}</div>
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

const hasData = computed(() => {
  const s = props.statistics || {};
  return s.goals || s.availability;
});
</script>
