<template>
  <div class="container mx-auto px-4 py-8 max-w-6xl">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-foreground">
        {{ isNewPlayer ? 'Add New Player' : 'Edit Player' }}
      </h1>
      <button
        @click="goBack"
        class="px-4 py-2 border border-input rounded-md hover:bg-accent text-foreground"
      >
        Back to List
      </button>
    </div>

    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading...</p>
    </div>

    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <div v-else class="bg-card rounded-lg shadow-lg">
      <!-- Tabs -->
      <div class="border-b border-border">
        <nav class="flex -mb-px">
          <button
            v-for="tab in tabs"
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="[
              'px-6 py-4 text-sm font-medium border-b-2 transition-colors',
              activeTab === tab.id
                ? 'border-primary text-primary'
                : 'border-transparent text-muted-foreground hover:text-foreground hover:border-border'
            ]"
          >
            {{ tab.label }}
          </button>
        </nav>
      </div>

      <!-- Tab Content -->
      <div class="p-6">
        <form @submit.prevent="savePlayer">
          <PlayerBasicInfo
            v-if="activeTab === 'basic'"
            :player="player"
            @update:player="player = $event"
          />

          <PlayerSkills
            v-if="activeTab === 'skills'"
            :skills="skills"
            @update:skills="skills = $event"
          />

          <PlayerStatistics
            v-if="activeTab === 'statistics'"
            :statistics="statistics"
            @update:statistics="statistics = $event"
          />

          <!-- Action Buttons -->
          <div class="flex justify-end space-x-4 mt-8 pt-6 border-t border-border">
            <button
              type="button"
              @click="goBack"
              class="px-6 py-2 border border-input rounded-md hover:bg-accent text-foreground"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="saving"
              class="px-6 py-2 bg-primary text-primary-foreground rounded-md hover:bg-primary/90 disabled:opacity-50"
            >
              {{ saving ? 'Saving...' : 'Save' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import PlayerBasicInfo from '../components/PlayerBasicInfo.vue';
import PlayerSkills from '../components/PlayerSkills.vue';
import PlayerStatistics from '../components/PlayerStatistics.vue';
import playerService from '../services/playerService';

const router = useRouter();
const route = useRoute();

const activeTab = ref('basic');
const loading = ref(true);
const saving = ref(false);
const error = ref(null);

const player = ref({
  name: '',
  email: '',
  graduationYear: null,
  major: '',
  city: '',
  country: '',
  phoneNumber: '',
  gender: ''
});

const skills = ref({
  forehand: null,
  backhand: null,
  baseline: null,
  volley: null,
  smash: null,
  serve: null,
  returnServe: null,
  mental: null,
  movement: null,
  fitness: null,
  courtPositioning: null,
  shotSelection: null,
  competitiveSpirit: null,
  strengths: '',
  weaknesses: '',
  notes: ''
});

const statistics = ref({
  utrRating: null,
  utrStatus: '',
  utrUrl: '',
  ntrpRating: null,
  ntrpStatus: '',
  ntrpUrl: '',
  dynamicRating: null,
  dynamicRatingUrl: '',
  selfRating: null,
  totalMatches: null,
  wins: null,
  losses: null,
  winRate: null,
  singlesWinRate: null,
  doublesWinRate: null,
  playFrequency: '',
  matchesPerMonth: null,
  practiceHoursPerWeek: null,
  competitiveLevel: '',
  tournamentParticipation: null,
  leagueParticipation: null,
  servePercentage: null,
  firstServePercentage: null,
  breakPointConversion: null,
  averageMatchDurationMinutes: null,
  preferredSurface: '',
  preferredPlayingStyle: '',
  dominantHand: '',
  preferredDoublesPosition: '',
  availability: '',
  goals: ''
});

const skillsExist = ref(false);
const statisticsExist = ref(false);

const tabs = [
  { id: 'basic', label: 'Basic Information' },
  { id: 'skills', label: 'Skills' },
  { id: 'statistics', label: 'Statistics' }
];

const isNewPlayer = computed(() => route.path === '/players/new' || route.params.id === 'new');

const loadPlayer = async () => {
  if (isNewPlayer.value) {
    loading.value = false;
    return;
  }

  try {
    loading.value = true;
    const playerId = parseInt(route.params.id);

    const playerData = await playerService.getPlayerById(playerId);
    player.value = playerData;

    // Load skills from player entity
    if (playerData.skills) {
      skills.value = playerData.skills;
      skillsExist.value = true;
    } else {
      skillsExist.value = false;
    }

    // Load statistics from player entity
    if (playerData.statistics) {
      statistics.value = playerData.statistics;
      statisticsExist.value = true;
    } else {
      statisticsExist.value = false;
    }

  } catch (err) {
    error.value = 'Failed to load player: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const savePlayer = async () => {
  try {
    saving.value = true;

    // Prepare player with nested skills and statistics
    const playerToSave = {
      ...player.value,
      skills: skills.value && Object.values(skills.value).some(v => v !== null && v !== '')
        ? skills.value
        : null,
      statistics: statistics.value && Object.values(statistics.value).some(v => v !== null && v !== '')
        ? statistics.value
        : null
    };

    if (isNewPlayer.value) {
      await playerService.createPlayer(playerToSave);
    } else {
      await playerService.updatePlayer(playerToSave.id, playerToSave);
    }

    router.push('/players');
  } catch (err) {
    alert('Failed to save player: ' + err.message);
  } finally {
    saving.value = false;
  }
};

const goBack = () => {
  router.push('/players');
};

onMounted(() => {
  loadPlayer();
});
</script>
