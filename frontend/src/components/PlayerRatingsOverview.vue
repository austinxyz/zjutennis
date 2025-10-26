<template>
  <div v-if="hasRatingsData" class="space-y-4">
    <h3 class="font-semibold text-sm">All Ratings</h3>

    <div class="space-y-3">
      <!-- Singles UTR Rating -->
      <div v-if="statistics.singlesUtrRating" class="flex items-center justify-between">
        <div class="flex items-center gap-2">
          <span class="text-sm text-muted-foreground">Singles UTR</span>
          <span v-if="statistics.singlesUtrStatus" class="text-xs px-1.5 py-0.5 rounded bg-blue-100 text-blue-800">
            {{ statistics.singlesUtrStatus }}
          </span>
        </div>
        <div class="flex items-center gap-2">
          <span class="text-lg font-bold text-blue-600">{{ statistics.singlesUtrRating.toFixed(2) }}</span>
          <a
            v-if="statistics.singlesUtrUrl"
            :href="statistics.singlesUtrUrl"
            target="_blank"
            class="text-primary hover:underline"
          >
            <ExternalLink class="h-3 w-3" />
          </a>
        </div>
      </div>

      <!-- Doubles UTR Rating -->
      <div v-if="statistics.utrRating" class="flex items-center justify-between">
        <div class="flex items-center gap-2">
          <span class="text-sm text-muted-foreground">Doubles UTR</span>
          <span v-if="statistics.utrStatus" class="text-xs px-1.5 py-0.5 rounded bg-blue-100 text-blue-800">
            {{ statistics.utrStatus }}
          </span>
        </div>
        <div class="flex items-center gap-2">
          <span class="text-lg font-bold text-blue-600">{{ statistics.utrRating.toFixed(2) }}</span>
          <a
            v-if="statistics.utrUrl"
            :href="statistics.utrUrl"
            target="_blank"
            class="text-primary hover:underline"
          >
            <ExternalLink class="h-3 w-3" />
          </a>
        </div>
      </div>

      <!-- NTRP Rating -->
      <div v-if="statistics.ntrpRating" class="flex items-center justify-between">
        <div class="flex items-center gap-2">
          <span class="text-sm text-muted-foreground">NTRP</span>
          <span v-if="statistics.ntrpStatus" class="text-xs px-1.5 py-0.5 rounded bg-green-100 text-green-800">
            {{ statistics.ntrpStatus }}
          </span>
        </div>
        <div class="flex items-center gap-2">
          <span class="text-lg font-bold text-green-600">{{ statistics.ntrpRating.toFixed(1) }}</span>
          <a
            v-if="statistics.ntrpUrl"
            :href="statistics.ntrpUrl"
            target="_blank"
            class="text-primary hover:underline"
          >
            <ExternalLink class="h-3 w-3" />
          </a>
        </div>
      </div>

      <!-- Dynamic Rating -->
      <div v-if="statistics.dynamicRating" class="flex items-center justify-between">
        <span class="text-sm text-muted-foreground">Dynamic</span>
        <div class="flex items-center gap-2">
          <span class="text-lg font-bold text-purple-600">{{ statistics.dynamicRating.toFixed(2) }}</span>
          <a
            v-if="statistics.dynamicRatingUrl"
            :href="statistics.dynamicRatingUrl"
            target="_blank"
            class="text-primary hover:underline"
          >
            <ExternalLink class="h-3 w-3" />
          </a>
        </div>
      </div>

      <!-- Self Rating -->
      <div v-if="statistics.selfRating" class="flex items-center justify-between">
        <span class="text-sm text-muted-foreground">Self Rating</span>
        <span class="text-lg font-bold text-gray-600">{{ statistics.selfRating.toFixed(1) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { ExternalLink } from 'lucide-vue-next';

const props = defineProps({
  statistics: {
    type: Object,
    default: () => ({})
  }
});

const hasRatingsData = computed(() => {
  const s = props.statistics || {};
  return s.singlesUtrRating || s.utrRating || s.ntrpRating || s.dynamicRating || s.selfRating;
});
</script>
