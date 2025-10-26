<template>
  <div class="space-y-6">
    <!-- Mental & Physical -->
    <div v-if="hasMentalPhysical">
      <h4 class="text-sm font-semibold mb-3">Mental & Physical</h4>
      <div class="space-y-3">
        <div v-if="skills.mental !== null && skills.mental !== undefined">
          <div class="flex justify-between text-sm mb-1">
            <span class="text-muted-foreground">Mental</span>
            <span class="font-medium">{{ skills.mental }}/4</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div
              class="h-full bg-purple-500 transition-all"
              :style="{ width: `${(skills.mental / 4) * 100}%` }"
            ></div>
          </div>
        </div>

        <div v-if="skills.movement !== null && skills.movement !== undefined">
          <div class="flex justify-between text-sm mb-1">
            <span class="text-muted-foreground">Movement</span>
            <span class="font-medium">{{ skills.movement }}/5</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div
              class="h-full bg-purple-500 transition-all"
              :style="{ width: `${(skills.movement / 5) * 100}%` }"
            ></div>
          </div>
        </div>

        <div v-if="skills.fitness !== null && skills.fitness !== undefined">
          <div class="flex justify-between text-sm mb-1">
            <span class="text-muted-foreground">Fitness</span>
            <span class="font-medium">{{ skills.fitness }}/10</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div
              class="h-full bg-purple-500 transition-all"
              :style="{ width: `${(skills.fitness / 10) * 100}%` }"
            ></div>
          </div>
        </div>
      </div>
    </div>

    <!-- Strategy & Tactics -->
    <div v-if="hasStrategyTactics">
      <h4 class="text-sm font-semibold mb-3">Strategy & Tactics</h4>
      <div class="space-y-3">
        <div v-if="skills.courtPositioning !== null && skills.courtPositioning !== undefined">
          <div class="flex justify-between text-sm mb-1">
            <span class="text-muted-foreground">Court Positioning</span>
            <span class="font-medium">{{ skills.courtPositioning }}/5</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div
              class="h-full bg-green-500 transition-all"
              :style="{ width: `${(skills.courtPositioning / 5) * 100}%` }"
            ></div>
          </div>
        </div>

        <div v-if="skills.shotSelection !== null && skills.shotSelection !== undefined">
          <div class="flex justify-between text-sm mb-1">
            <span class="text-muted-foreground">Shot Selection</span>
            <span class="font-medium">{{ skills.shotSelection }}/5</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div
              class="h-full bg-green-500 transition-all"
              :style="{ width: `${(skills.shotSelection / 5) * 100}%` }"
            ></div>
          </div>
        </div>

        <div v-if="skills.competitiveSpirit !== null && skills.competitiveSpirit !== undefined">
          <div class="flex justify-between text-sm mb-1">
            <span class="text-muted-foreground">Competitive Spirit</span>
            <span class="font-medium">{{ skills.competitiveSpirit }}/5</span>
          </div>
          <div class="h-2 bg-muted rounded-full overflow-hidden">
            <div
              class="h-full bg-green-500 transition-all"
              :style="{ width: `${(skills.competitiveSpirit / 5) * 100}%` }"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  skills: {
    type: Object,
    default: () => ({})
  }
});

const hasMentalPhysical = computed(() => {
  const s = props.skills || {};
  return (s.mental !== null && s.mental !== undefined) ||
         (s.movement !== null && s.movement !== undefined) ||
         (s.fitness !== null && s.fitness !== undefined);
});

const hasStrategyTactics = computed(() => {
  const s = props.skills || {};
  return (s.courtPositioning !== null && s.courtPositioning !== undefined) ||
         (s.shotSelection !== null && s.shotSelection !== undefined) ||
         (s.competitiveSpirit !== null && s.competitiveSpirit !== undefined);
});
</script>
