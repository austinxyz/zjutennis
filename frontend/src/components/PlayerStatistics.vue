<template>
  <div class="space-y-6">
    <!-- Rating Systems -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Rating Systems</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">UTR Rating</label>
          <input
            v-model.number="localStats.utrRating"
            type="number"
            step="0.01"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">UTR Status</label>
          <select
            v-model="localStats.utrStatus"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select status</option>
            <option value="rated">Rated</option>
            <option value="projected">Projected</option>
            <option value="unrated">Unrated</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">UTR URL</label>
          <input
            v-model="localStats.utrUrl"
            type="url"
            placeholder="https://app.myutr.com/profiles/..."
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">NTRP Rating</label>
          <input
            v-model.number="localStats.ntrpRating"
            type="number"
            step="0.5"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">NTRP Status</label>
          <select
            v-model="localStats.ntrpStatus"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select status</option>
            <option value="C">C - Computed</option>
            <option value="S">S - Self Rating</option>
            <option value="D">D - Disqualified</option>
            <option value="A">A - Appealed</option>
            <option value="M">M - Mixed</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">NTRP URL</label>
          <input
            v-model="localStats.ntrpUrl"
            type="url"
            placeholder="https://tennislink.usta.com/..."
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Dynamic Rating</label>
          <input
            v-model.number="localStats.dynamicRating"
            type="number"
            step="0.01"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Dynamic Rating URL</label>
          <input
            v-model="localStats.dynamicRatingUrl"
            type="url"
            placeholder="https://..."
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Self Rating</label>
          <input
            v-model.number="localStats.selfRating"
            type="number"
            step="0.5"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
      </div>
    </div>

    <!-- Match Statistics -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Match Statistics</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Total Matches</label>
          <input
            v-model.number="localStats.totalMatches"
            type="number"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Wins</label>
          <input
            v-model.number="localStats.wins"
            type="number"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Losses</label>
          <input
            v-model.number="localStats.losses"
            type="number"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Win Rate (%)</label>
          <input
            v-model.number="localStats.winRate"
            type="number"
            step="0.1"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Singles Win Rate (%)</label>
          <input
            v-model.number="localStats.singlesWinRate"
            type="number"
            step="0.1"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Doubles Win Rate (%)</label>
          <input
            v-model.number="localStats.doublesWinRate"
            type="number"
            step="0.1"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
      </div>
    </div>

    <!-- Activity Level -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Activity Level</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Play Frequency</label>
          <select
            v-model="localStats.playFrequency"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select frequency</option>
            <option value="daily">Daily</option>
            <option value="weekly">Weekly</option>
            <option value="monthly">Monthly</option>
            <option value="occasionally">Occasionally</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Matches per Month</label>
          <input
            v-model.number="localStats.matchesPerMonth"
            type="number"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Practice Hours/Week</label>
          <input
            v-model.number="localStats.practiceHoursPerWeek"
            type="number"
            step="0.5"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
      </div>
    </div>

    <!-- Competitive Level -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Competitive Level</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Competitive Level</label>
          <select
            v-model="localStats.competitiveLevel"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select level</option>
            <option value="recreational">Recreational</option>
            <option value="intermediate">Intermediate</option>
            <option value="advanced">Advanced</option>
            <option value="professional">Professional</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Tournament Participation</label>
          <select
            v-model="localStats.tournamentParticipation"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option :value="null">Select</option>
            <option :value="true">Yes</option>
            <option :value="false">No</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">League Participation</label>
          <select
            v-model="localStats.leagueParticipation"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option :value="null">Select</option>
            <option :value="true">Yes</option>
            <option :value="false">No</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Performance Metrics -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Performance Metrics</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Serve Percentage (%)</label>
          <input
            v-model.number="localStats.servePercentage"
            type="number"
            step="0.1"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">First Serve Percentage (%)</label>
          <input
            v-model.number="localStats.firstServePercentage"
            type="number"
            step="0.1"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Break Point Conversion (%)</label>
          <input
            v-model.number="localStats.breakPointConversion"
            type="number"
            step="0.1"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Avg Match Duration (min)</label>
          <input
            v-model.number="localStats.averageMatchDurationMinutes"
            type="number"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          />
        </div>
      </div>
    </div>

    <!-- Playing Preferences -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Playing Preferences</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Preferred Surface</label>
          <select
            v-model="localStats.preferredSurface"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select surface</option>
            <option value="hard">Hard</option>
            <option value="clay">Clay</option>
            <option value="grass">Grass</option>
            <option value="carpet">Carpet</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Preferred Playing Style</label>
          <select
            v-model="localStats.preferredPlayingStyle"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select style</option>
            <option value="baseline">Baseline</option>
            <option value="serve-volley">Serve & Volley</option>
            <option value="all-court">All-Court</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Dominant Hand</label>
          <select
            v-model="localStats.dominantHand"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select hand</option>
            <option value="right">Right</option>
            <option value="left">Left</option>
            <option value="ambidextrous">Ambidextrous</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Preferred Doubles Position</label>
          <select
            v-model="localStats.preferredDoublesPosition"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          >
            <option value="">Select position</option>
            <option value="ad-court">Ad Court</option>
            <option value="deuce-court">Deuce Court</option>
            <option value="both">Both</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Additional Information -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Additional Information</h3>
      <div class="grid grid-cols-1 gap-6">
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Availability</label>
          <textarea
            v-model="localStats.availability"
            rows="2"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          ></textarea>
        </div>
        <div>
          <label class="block text-sm font-medium text-foreground mb-2">Goals</label>
          <textarea
            v-model="localStats.goals"
            rows="3"
            class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
          ></textarea>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  statistics: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update:statistics']);

const localStats = ref({ ...props.statistics });

watch(localStats, (newValue) => {
  emit('update:statistics', newValue);
}, { deep: true });

watch(() => props.statistics, (newValue) => {
  localStats.value = { ...newValue };
}, { deep: true });
</script>
