<template>
  <div class="space-y-6">
    <!-- Rating Systems -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Rating Systems</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="space-y-2">
          <Label for="utrRating">UTR Rating</Label>
          <Input
            id="utrRating"
            v-model.number="localStats.utrRating"
            type="number"
            step="0.01"
            placeholder="0.00"
          />
        </div>
        <div class="space-y-2">
          <Label for="utrStatus">UTR Status</Label>
          <select
            id="utrStatus"
            v-model="localStats.utrStatus"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select status</option>
            <option value="rated">Rated</option>
            <option value="projected">Projected</option>
            <option value="unrated">Unrated</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="utrUrl">UTR URL</Label>
          <Input
            id="utrUrl"
            v-model="localStats.utrUrl"
            type="url"
            placeholder="https://app.myutr.com/profiles/..."
          />
        </div>
        <div class="space-y-2">
          <Label for="ntrpRating">NTRP Rating</Label>
          <Input
            id="ntrpRating"
            v-model.number="localStats.ntrpRating"
            type="number"
            step="0.5"
            placeholder="0.0"
          />
        </div>
        <div class="space-y-2">
          <Label for="ntrpStatus">NTRP Status</Label>
          <select
            id="ntrpStatus"
            v-model="localStats.ntrpStatus"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select status</option>
            <option value="C">C - Computed</option>
            <option value="S">S - Self Rating</option>
            <option value="D">D - Disqualified</option>
            <option value="A">A - Appealed</option>
            <option value="M">M - Mixed</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="ntrpUrl">NTRP URL</Label>
          <Input
            id="ntrpUrl"
            v-model="localStats.ntrpUrl"
            type="url"
            placeholder="https://tennislink.usta.com/..."
          />
        </div>
        <div class="space-y-2">
          <Label for="dynamicRating">Dynamic Rating</Label>
          <Input
            id="dynamicRating"
            v-model.number="localStats.dynamicRating"
            type="number"
            step="0.01"
            placeholder="0.00"
          />
        </div>
        <div class="space-y-2">
          <Label for="dynamicRatingUrl">Dynamic Rating URL</Label>
          <Input
            id="dynamicRatingUrl"
            v-model="localStats.dynamicRatingUrl"
            type="url"
            placeholder="https://..."
          />
        </div>
        <div class="space-y-2">
          <Label for="selfRating">Self Rating</Label>
          <Input
            id="selfRating"
            v-model.number="localStats.selfRating"
            type="number"
            step="0.5"
            placeholder="0.0"
          />
        </div>
      </div>
    </div>

    <!-- Match Statistics -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Match Statistics</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="space-y-2">
          <Label for="totalMatches">Total Matches</Label>
          <Input
            id="totalMatches"
            v-model.number="localStats.totalMatches"
            type="number"
            placeholder="0"
          />
        </div>
        <div class="space-y-2">
          <Label for="wins">Wins</Label>
          <Input
            id="wins"
            v-model.number="localStats.wins"
            type="number"
            placeholder="0"
          />
        </div>
        <div class="space-y-2">
          <Label for="losses">Losses</Label>
          <Input
            id="losses"
            v-model.number="localStats.losses"
            type="number"
            placeholder="0"
          />
        </div>
        <div class="space-y-2">
          <Label for="winRate">Win Rate (%)</Label>
          <Input
            id="winRate"
            v-model.number="localStats.winRate"
            type="number"
            step="0.1"
            placeholder="0.0"
          />
        </div>
        <div class="space-y-2">
          <Label for="singlesWinRate">Singles Win Rate (%)</Label>
          <Input
            id="singlesWinRate"
            v-model.number="localStats.singlesWinRate"
            type="number"
            step="0.1"
            placeholder="0.0"
          />
        </div>
        <div class="space-y-2">
          <Label for="doublesWinRate">Doubles Win Rate (%)</Label>
          <Input
            id="doublesWinRate"
            v-model.number="localStats.doublesWinRate"
            type="number"
            step="0.1"
            placeholder="0.0"
          />
        </div>
      </div>
    </div>

    <!-- Activity Level -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Activity Level</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="space-y-2">
          <Label for="playFrequency">Play Frequency</Label>
          <select
            id="playFrequency"
            v-model="localStats.playFrequency"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select frequency</option>
            <option value="daily">Daily</option>
            <option value="weekly">Weekly</option>
            <option value="monthly">Monthly</option>
            <option value="occasionally">Occasionally</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="matchesPerMonth">Matches per Month</Label>
          <Input
            id="matchesPerMonth"
            v-model.number="localStats.matchesPerMonth"
            type="number"
            placeholder="0"
          />
        </div>
        <div class="space-y-2">
          <Label for="practiceHoursPerWeek">Practice Hours/Week</Label>
          <Input
            id="practiceHoursPerWeek"
            v-model.number="localStats.practiceHoursPerWeek"
            type="number"
            step="0.5"
            placeholder="0.0"
          />
        </div>
      </div>
    </div>

    <!-- Competitive Level -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Competitive Level</h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="space-y-2">
          <Label for="competitiveLevel">Competitive Level</Label>
          <select
            id="competitiveLevel"
            v-model="localStats.competitiveLevel"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select level</option>
            <option value="recreational">Recreational</option>
            <option value="intermediate">Intermediate</option>
            <option value="advanced">Advanced</option>
            <option value="professional">Professional</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="tournamentParticipation">Tournament Participation</Label>
          <select
            id="tournamentParticipation"
            v-model="localStats.tournamentParticipation"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option :value="null">Select</option>
            <option :value="true">Yes</option>
            <option :value="false">No</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="leagueParticipation">League Participation</Label>
          <select
            id="leagueParticipation"
            v-model="localStats.leagueParticipation"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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
        <div class="space-y-2">
          <Label for="servePercentage">Serve Percentage (%)</Label>
          <Input
            id="servePercentage"
            v-model.number="localStats.servePercentage"
            type="number"
            step="0.1"
            placeholder="0.0"
          />
        </div>
        <div class="space-y-2">
          <Label for="firstServePercentage">First Serve Percentage (%)</Label>
          <Input
            id="firstServePercentage"
            v-model.number="localStats.firstServePercentage"
            type="number"
            step="0.1"
            placeholder="0.0"
          />
        </div>
        <div class="space-y-2">
          <Label for="breakPointConversion">Break Point Conversion (%)</Label>
          <Input
            id="breakPointConversion"
            v-model.number="localStats.breakPointConversion"
            type="number"
            step="0.1"
            placeholder="0.0"
          />
        </div>
        <div class="space-y-2">
          <Label for="averageMatchDurationMinutes">Avg Match Duration (min)</Label>
          <Input
            id="averageMatchDurationMinutes"
            v-model.number="localStats.averageMatchDurationMinutes"
            type="number"
            placeholder="0"
          />
        </div>
      </div>
    </div>

    <!-- Playing Preferences -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Playing Preferences</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="space-y-2">
          <Label for="preferredSurface">Preferred Surface</Label>
          <select
            id="preferredSurface"
            v-model="localStats.preferredSurface"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select surface</option>
            <option value="hard">Hard</option>
            <option value="clay">Clay</option>
            <option value="grass">Grass</option>
            <option value="carpet">Carpet</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="preferredPlayingStyle">Preferred Playing Style</Label>
          <select
            id="preferredPlayingStyle"
            v-model="localStats.preferredPlayingStyle"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select style</option>
            <option value="baseline">Baseline</option>
            <option value="serve-volley">Serve & Volley</option>
            <option value="all-court">All-Court</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="dominantHand">Dominant Hand</Label>
          <select
            id="dominantHand"
            v-model="localStats.dominantHand"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="">Select hand</option>
            <option value="right">Right</option>
            <option value="left">Left</option>
            <option value="ambidextrous">Ambidextrous</option>
          </select>
        </div>
        <div class="space-y-2">
          <Label for="preferredDoublesPosition">Preferred Doubles Position</Label>
          <select
            id="preferredDoublesPosition"
            v-model="localStats.preferredDoublesPosition"
            class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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
        <div class="space-y-2">
          <Label for="availability">Availability</Label>
          <Textarea
            id="availability"
            v-model="localStats.availability"
            :rows="2"
            placeholder="Describe when you're available to play..."
          />
        </div>
        <div class="space-y-2">
          <Label for="goals">Goals</Label>
          <Textarea
            id="goals"
            v-model="localStats.goals"
            :rows="3"
            placeholder="Describe your tennis goals and objectives..."
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import Label from './ui/Label.vue';
import Input from './ui/Input.vue';
import Textarea from './ui/Textarea.vue';

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
