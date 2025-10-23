<template>
  <div class="container mx-auto px-4 py-8 max-w-6xl">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-foreground">
        {{ isNewPlayer ? 'Add New Player' : 'Edit Player' }}
      </h1>
      <Button
        @click="goBack"
        variant="outline"
      >
        <ArrowLeft class="mr-2 h-4 w-4" />
        Back to List
      </Button>
    </div>

    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading...</p>
    </div>

    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <Card v-else>
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

          <PlayerAlumni
            v-if="activeTab === 'alumni'"
            :alumni="alumni"
            @update:alumni="alumni = $event"
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
            <Button
              type="button"
              @click="goBack"
              variant="outline"
            >
              Cancel
            </Button>
            <Button
              type="submit"
              :disabled="saving"
            >
              <Save class="mr-2 h-4 w-4" />
              {{ saving ? 'Saving...' : 'Save' }}
            </Button>
          </div>
        </form>
      </div>
    </Card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ArrowLeft, Save } from 'lucide-vue-next';
import PlayerBasicInfo from '../components/PlayerBasicInfo.vue';
import PlayerSkills from '../components/PlayerSkills.vue';
import PlayerStatistics from '../components/PlayerStatistics.vue';
import PlayerAlumni from '../components/PlayerAlumni.vue';
import playerService from '../services/playerService';
import Button from '../components/ui/Button.vue';
import Card from '../components/ui/Card.vue';

const router = useRouter();
const route = useRoute();

const activeTab = ref('basic');
const loading = ref(true);
const saving = ref(false);
const error = ref(null);

const player = ref({
  name: '',
  email: '',
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
  utrUpdatedDate: null,
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

const alumni = ref({
  graduationUniversity1: '',
  graduationYear1: null,
  graduationUniversity2: '',
  graduationYear2: null,
  graduationUniversity3: '',
  graduationYear3: null,
  coupleGraduationUniversity1: '',
  coupleGraduationYear1: null,
  coupleGraduationUniversity2: '',
  coupleGraduationYear2: null,
  coupleGraduationUniversity3: '',
  coupleGraduationYear3: null
});

const skillsExist = ref(false);
const statisticsExist = ref(false);
const alumniExist = ref(false);

const tabs = [
  { id: 'basic', label: 'Basic Information' },
  { id: 'alumni', label: 'Alumni Information' },
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

    if (playerData.skills) {
      skills.value = playerData.skills;
      skillsExist.value = true;
    } else {
      skillsExist.value = false;
    }

    if (playerData.statistics) {
      statistics.value = playerData.statistics;
      statisticsExist.value = true;
    } else {
      statisticsExist.value = false;
    }

    if (playerData.alumni) {
      alumni.value = playerData.alumni;
      alumniExist.value = true;
    } else {
      alumniExist.value = false;
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

    const playerToSave = {
      ...player.value,
      skills: skills.value && Object.values(skills.value).some(v => v !== null && v !== '')
        ? skills.value
        : null,
      statistics: statistics.value && Object.values(statistics.value).some(v => v !== null && v !== '')
        ? statistics.value
        : null,
      alumni: alumni.value && Object.values(alumni.value).some(v => v !== null && v !== '')
        ? alumni.value
        : null
    };

    if (isNewPlayer.value) {
      const savedPlayer = await playerService.createPlayer(playerToSave);
      // Update the route to the new player's ID without redirecting to list
      router.replace(`/players/${savedPlayer.id}`);
      // Reload the player data to get the saved version
      await loadPlayer();
    } else {
      await playerService.updatePlayer(playerToSave.id, playerToSave);
      // Reload the player data to get the updated version (including utr_updated_date)
      await loadPlayer();
    }

    // Show success message
    alert('Player saved successfully!');
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
