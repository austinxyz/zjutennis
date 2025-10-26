<template>
  <div v-if="hasActivityData" class="space-y-4">
    <h3 class="font-semibold text-sm">Activity Level</h3>

    <!-- Play Frequency -->
    <div v-if="statistics.playFrequency" class="space-y-2">
      <div class="flex items-center justify-between text-sm">
        <span class="text-muted-foreground">Play Frequency</span>
        <span class="font-medium capitalize">{{ statistics.playFrequency }}</span>
      </div>
    </div>

    <!-- Participation Badges -->
    <div class="flex gap-2 flex-wrap">
      <div v-if="statistics.tournamentParticipation" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
        Tournament Player
      </div>
      <div v-if="statistics.leagueParticipation" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
        League Player
      </div>
    </div>

    <!-- Last Match Date -->
    <div v-if="statistics.lastMatchDate" class="space-y-2">
      <div class="flex items-center justify-between text-sm">
        <span class="text-muted-foreground">Last Match</span>
        <span class="font-medium">{{ formatDate(statistics.lastMatchDate) }}</span>
      </div>
    </div>

    <!-- Competitive Level -->
    <div v-if="statistics.competitiveLevel" class="space-y-2">
      <div class="flex items-center justify-between text-sm">
        <span class="text-muted-foreground">Level</span>
        <span class="font-medium capitalize">{{ statistics.competitiveLevel }}</span>
      </div>
    </div>

    <!-- UTR Update Date -->
    <div v-if="statistics.utrUpdatedDate" class="space-y-2 pt-2 border-t">
      <div class="flex items-center justify-between text-xs text-muted-foreground">
        <span>UTR Updated</span>
        <span>{{ formatDate(statistics.utrUpdatedDate) }}</span>
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

const hasActivityData = computed(() => {
  const s = props.statistics || {};
  return s.playFrequency ||
         s.tournamentParticipation !== null ||
         s.leagueParticipation !== null ||
         s.lastMatchDate ||
         s.competitiveLevel ||
         s.utrUpdatedDate;
});

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const now = new Date();
  const diffTime = Math.abs(now - date);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  if (diffDays === 0) return 'Today';
  if (diffDays === 1) return 'Yesterday';
  if (diffDays < 7) return `${diffDays} days ago`;
  if (diffDays < 30) return `${Math.floor(diffDays / 7)} weeks ago`;
  if (diffDays < 365) return `${Math.floor(diffDays / 30)} months ago`;

  return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
};
</script>
