<template>
  <div class="h-full flex flex-col">
    <!-- Header with Actions -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Player Management</h1>
        <p class="text-sm text-muted-foreground mt-1">
          {{ totalCount }} player{{ totalCount !== 1 ? 's' : '' }} found
        </p>
      </div>
      <div class="flex gap-3">
        <Button
          v-if="selectedPlayers.length > 0"
          @click="clearSelection"
          variant="outline"
        >
          Clear Selection ({{ selectedPlayers.length }})
        </Button>
        <Button
          @click="exportToCSV"
          variant="outline"
        >
          Export {{ selectedPlayers.length > 0 ? `Selected (${selectedPlayers.length})` : 'All' }}
        </Button>
        <Label class="cursor-pointer">
          <Button variant="outline" as="span">
            Import CSV
          </Button>
          <input
            type="file"
            ref="fileInput"
            @change="handleFileUpload"
            accept=".csv"
            class="hidden"
          />
        </Label>
        <Button @click="goToNewPlayer">
          <Plus class="mr-2 h-4 w-4" />
          Add New Player
        </Button>
      </div>
    </div>

    <!-- Simple Search Bar -->
    <div class="mb-4">
      <div class="flex gap-2">
        <div class="flex-1">
          <Input
            v-model="simpleSearch"
            placeholder="Search by player name..."
            type="text"
            class="text-base"
          >
            <template #prefix>
              <Search class="h-4 w-4 text-muted-foreground" />
            </template>
          </Input>
        </div>
        <Button
          @click="showAdvancedSearch = !showAdvancedSearch"
          variant="outline"
        >
          <ChevronDown v-if="!showAdvancedSearch" class="h-4 w-4 mr-2" />
          <ChevronUp v-else class="h-4 w-4 mr-2" />
          Advanced Filters
        </Button>
      </div>
    </div>

    <!-- Advanced Search Panel (Collapsible) -->
    <Card v-if="showAdvancedSearch" class="mb-4">
      <div class="p-4">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- Gender -->
          <div class="space-y-2">
            <Label for="gender">Gender</Label>
            <select
              id="gender"
              v-model="filters.gender"
              class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            >
              <option value="">All</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
            </select>
          </div>

          <!-- UTR Range -->
          <div class="space-y-2">
            <Label>UTR Range</Label>
            <div class="flex gap-2">
              <Input
                v-model.number="filters.utrMin"
                placeholder="Min"
                type="number"
                step="0.1"
                class="w-1/2"
              />
              <Input
                v-model.number="filters.utrMax"
                placeholder="Max"
                type="number"
                step="0.1"
                class="w-1/2"
              />
            </div>
          </div>

          <!-- NTRP -->
          <div class="space-y-2">
            <Label for="ntrp">NTRP Level</Label>
            <select
              id="ntrp"
              v-model="filters.ntrp"
              class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            >
              <option value="">All</option>
              <option value="2.5">2.5</option>
              <option value="3.0">3.0</option>
              <option value="3.5">3.5</option>
              <option value="4.0">4.0</option>
              <option value="4.5">4.5</option>
              <option value="5.0">5.0</option>
              <option value="5.5">5.5</option>
            </select>
          </div>

          <!-- Win Rate -->
          <div class="space-y-2">
            <Label>Win Rate (%)</Label>
            <div class="flex gap-2">
              <Input
                v-model.number="filters.winRateMin"
                placeholder="Min"
                type="number"
                class="w-1/2"
              />
              <Input
                v-model.number="filters.winRateMax"
                placeholder="Max"
                type="number"
                class="w-1/2"
              />
            </div>
          </div>

          <!-- University -->
          <div class="space-y-2">
            <Label for="university">University</Label>
            <Input
              id="university"
              v-model="filters.university"
              placeholder="Search university..."
              type="text"
            />
          </div>

          <!-- City -->
          <div class="space-y-2">
            <Label for="city">City</Label>
            <Input
              id="city"
              v-model="filters.city"
              placeholder="Search city..."
              type="text"
            />
          </div>

          <!-- Country -->
          <div class="space-y-2">
            <Label for="country">Country</Label>
            <Input
              id="country"
              v-model="filters.country"
              placeholder="Search country..."
              type="text"
            />
          </div>

          <!-- Reset Button -->
          <div class="space-y-2 flex items-end">
            <Button
              @click="resetFilters"
              variant="outline"
              class="w-full"
            >
              Reset Filters
            </Button>
          </div>
        </div>
      </div>
    </Card>

    <!-- Loading State -->
    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading players...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <!-- Main Content: Player List + Details -->
    <div v-else class="flex-1 flex gap-6 overflow-hidden">
      <!-- Left: Player List -->
      <div class="w-96 flex-shrink-0 flex flex-col">
        <Card class="flex-1 flex flex-col overflow-hidden">
          <div class="p-4 border-b">
            <h2 class="font-semibold">Players ({{ players.length }})</h2>
          </div>
          <div class="flex-1 overflow-y-auto">
            <div v-if="players.length === 0" class="p-8 text-center text-muted-foreground">
              No players found
            </div>
            <div
              v-for="player in players"
              :key="player.id"
              @click="selectedPlayerId = player.id"
              class="p-4 border-b cursor-pointer hover:bg-accent transition-colors"
              :class="{ 'bg-accent': selectedPlayerId === player.id }"
            >
              <div class="flex items-center justify-between">
                <div class="flex-1">
                  <div class="flex items-center gap-2">
                    <input
                      type="checkbox"
                      :value="player.id"
                      v-model="selectedPlayers"
                      @click.stop
                      class="h-4 w-4 rounded border-input"
                    />
                    <h3 class="font-medium">{{ player.name }}</h3>
                  </div>
                  <div class="mt-1 text-sm text-muted-foreground">
                    <span v-if="player.statistics?.utrRating">
                      UTR: {{ player.statistics.utrRating.toFixed(2) }}
                    </span>
                    <span v-if="player.statistics?.ntrpRating" class="ml-2">
                      | NTRP: {{ player.statistics.ntrpRating.toFixed(1) }}
                    </span>
                  </div>
                  <div v-if="player.city || player.country" class="mt-1 text-xs text-muted-foreground">
                    {{ player.city }}{{ player.city && player.country ? ', ' : '' }}{{ player.country }}
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination -->
          <div v-if="totalPages > 1" class="p-4 border-t">
            <div class="flex items-center justify-between text-sm mb-2">
              <span class="text-muted-foreground">
                Page {{ currentPage }} of {{ totalPages }}
              </span>
            </div>
            <div class="flex items-center justify-center gap-1">
              <Button
                @click="currentPage = 1"
                :disabled="currentPage === 1"
                variant="outline"
                size="sm"
              >
                First
              </Button>
              <Button
                @click="currentPage--"
                :disabled="currentPage === 1"
                variant="outline"
                size="sm"
              >
                Prev
              </Button>
              <Button
                @click="currentPage++"
                :disabled="currentPage === totalPages"
                variant="outline"
                size="sm"
              >
                Next
              </Button>
              <Button
                @click="currentPage = totalPages"
                :disabled="currentPage === totalPages"
                variant="outline"
                size="sm"
              >
                Last
              </Button>
            </div>
          </div>
        </Card>
      </div>

      <!-- Right: Player Details -->
      <div class="flex-1 flex flex-col overflow-hidden">
        <Card v-if="selectedPlayer" class="flex-1 flex flex-col overflow-hidden">
          <div class="p-6 border-b">
            <div class="flex justify-between items-start">
              <div>
                <h2 class="text-2xl font-bold">{{ selectedPlayer.name }}</h2>
                <div class="flex gap-2 mt-2">
                  <Badge v-if="selectedPlayer.gender" variant="outline">
                    {{ selectedPlayer.gender.charAt(0).toUpperCase() + selectedPlayer.gender.slice(1) }}
                  </Badge>
                </div>
              </div>
              <Button @click="editPlayer(selectedPlayer.id)" size="sm">
                <Pencil class="h-4 w-4 mr-2" />
                Edit
              </Button>
            </div>
          </div>

          <div class="flex-1 overflow-y-auto p-6 space-y-6">
            <!-- Contact Information -->
            <div>
              <h3 class="font-semibold mb-3">Contact Information</h3>
              <div class="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <span class="text-muted-foreground">Email:</span>
                  <p>{{ selectedPlayer.email || '-' }}</p>
                </div>
                <div>
                  <span class="text-muted-foreground">Phone:</span>
                  <p>{{ selectedPlayer.phoneNumber || '-' }}</p>
                </div>
                <div>
                  <span class="text-muted-foreground">City:</span>
                  <p>{{ selectedPlayer.city || '-' }}</p>
                </div>
                <div>
                  <span class="text-muted-foreground">Country:</span>
                  <p>{{ selectedPlayer.country || '-' }}</p>
                </div>
              </div>
            </div>

            <!-- Alumni Information -->
            <div v-if="selectedPlayer.alumni">
              <h3 class="font-semibold mb-3">Education</h3>
              <div class="space-y-2 text-sm">
                <div v-if="selectedPlayer.alumni.graduationUniversity1">
                  <span class="text-muted-foreground">University:</span>
                  <p>{{ selectedPlayer.alumni.graduationUniversity1 }}
                    <span v-if="selectedPlayer.alumni.graduationYear1">({{ selectedPlayer.alumni.graduationYear1 }})</span>
                  </p>
                </div>
                <div v-if="selectedPlayer.alumni.graduationUniversity2">
                  <p>{{ selectedPlayer.alumni.graduationUniversity2 }}
                    <span v-if="selectedPlayer.alumni.graduationYear2">({{ selectedPlayer.alumni.graduationYear2 }})</span>
                  </p>
                </div>
              </div>
            </div>

            <!-- Statistics -->
            <div v-if="selectedPlayer.statistics">
              <h3 class="font-semibold mb-3">Ratings & Statistics</h3>
              <div class="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <span class="text-muted-foreground">UTR:</span>
                  <p>
                    <a
                      v-if="selectedPlayer.statistics.utrUrl"
                      :href="selectedPlayer.statistics.utrUrl"
                      target="_blank"
                      class="text-primary hover:underline inline-flex items-center gap-1"
                    >
                      {{ formatUTR(selectedPlayer.statistics) }}
                      <ExternalLink class="h-3 w-3" />
                    </a>
                    <span v-else>{{ formatUTR(selectedPlayer.statistics) }}</span>
                  </p>
                </div>
                <div>
                  <span class="text-muted-foreground">NTRP:</span>
                  <p>
                    <a
                      v-if="selectedPlayer.statistics.ntrpUrl"
                      :href="selectedPlayer.statistics.ntrpUrl"
                      target="_blank"
                      class="text-primary hover:underline inline-flex items-center gap-1"
                    >
                      {{ formatNTRP(selectedPlayer.statistics) }}
                      <ExternalLink class="h-3 w-3" />
                    </a>
                    <span v-else>{{ formatNTRP(selectedPlayer.statistics) }}</span>
                  </p>
                </div>
                <div>
                  <span class="text-muted-foreground">Win Rate:</span>
                  <p>{{ selectedPlayer.statistics.winRate !== null ? selectedPlayer.statistics.winRate.toFixed(1) + '%' : '-' }}</p>
                </div>
                <div>
                  <span class="text-muted-foreground">Total Matches:</span>
                  <p>{{ selectedPlayer.statistics.totalMatches || '-' }}</p>
                </div>
                <div>
                  <span class="text-muted-foreground">Wins:</span>
                  <p>{{ selectedPlayer.statistics.wins || '-' }}</p>
                </div>
                <div>
                  <span class="text-muted-foreground">Losses:</span>
                  <p>{{ selectedPlayer.statistics.losses || '-' }}</p>
                </div>
              </div>
            </div>

            <!-- Skills -->
            <div v-if="selectedPlayer.skills">
              <h3 class="font-semibold mb-3">Skills</h3>
              <div class="grid grid-cols-3 gap-4 text-sm">
                <div v-if="selectedPlayer.skills.forehand">
                  <span class="text-muted-foreground">Forehand:</span>
                  <p>{{ selectedPlayer.skills.forehand }}/10</p>
                </div>
                <div v-if="selectedPlayer.skills.backhand">
                  <span class="text-muted-foreground">Backhand:</span>
                  <p>{{ selectedPlayer.skills.backhand }}/10</p>
                </div>
                <div v-if="selectedPlayer.skills.serve">
                  <span class="text-muted-foreground">Serve:</span>
                  <p>{{ selectedPlayer.skills.serve }}/10</p>
                </div>
              </div>
              <div v-if="selectedPlayer.skills.strengths" class="mt-4">
                <span class="text-muted-foreground">Strengths:</span>
                <p class="mt-1">{{ selectedPlayer.skills.strengths }}</p>
              </div>
              <div v-if="selectedPlayer.skills.weaknesses" class="mt-4">
                <span class="text-muted-foreground">Weaknesses:</span>
                <p class="mt-1">{{ selectedPlayer.skills.weaknesses }}</p>
              </div>
            </div>
          </div>
        </Card>

        <!-- No Player Selected -->
        <Card v-else class="flex-1 flex items-center justify-center">
          <div class="text-center text-muted-foreground">
            <Users class="h-16 w-16 mx-auto mb-4 opacity-50" />
            <p>Select a player to view details</p>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { Plus, Pencil, ExternalLink, Search, ChevronDown, ChevronUp, Users } from 'lucide-vue-next';
import playerService from '../services/playerService';
import Button from '../components/ui/Button.vue';
import Card from '../components/ui/Card.vue';
import Badge from '../components/ui/Badge.vue';
import Label from '../components/ui/Label.vue';
import Input from '../components/ui/Input.vue';

const router = useRouter();
const players = ref([]);
const selectedPlayers = ref([]);
const selectedPlayerId = ref(null);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const pageSize = 10;
const totalCount = ref(0);
const totalPages = ref(0);
const simpleSearch = ref('');
const showAdvancedSearch = ref(false);

// Advanced filters
const filters = ref({
  gender: '',
  utrMin: null,
  utrMax: null,
  ntrp: '',
  winRateMin: null,
  winRateMax: null,
  university: '',
  city: '',
  country: ''
});

const loadPlayers = async () => {
  try {
    loading.value = true;
    const searchRequest = {
      name: simpleSearch.value || null,
      gender: filters.value.gender || null,
      utrMin: filters.value.utrMin || null,
      utrMax: filters.value.utrMax || null,
      ntrp: filters.value.ntrp ? parseFloat(filters.value.ntrp) : null,
      winRateMin: filters.value.winRateMin || null,
      winRateMax: filters.value.winRateMax || null,
      university: filters.value.university || null,
      city: filters.value.city || null,
      country: filters.value.country || null,
      page: currentPage.value,
      pageSize: pageSize,
      sortBy: null,
      sortOrder: 'desc'
    };
    const response = await playerService.searchPlayers(searchRequest);
    players.value = response.players;
    totalCount.value = response.totalCount;
    totalPages.value = response.totalPages;

    // Auto-select first player if none selected
    if (players.value.length > 0 && !selectedPlayerId.value) {
      selectedPlayerId.value = players.value[0].id;
    }
  } catch (err) {
    error.value = 'Failed to load players: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const selectedPlayer = computed(() => {
  return players.value.find(p => p.id === selectedPlayerId.value) || null;
});

// Watch for search changes
watch(() => simpleSearch.value, () => {
  currentPage.value = 1;
  loadPlayers();
});

watch(() => filters.value, () => {
  currentPage.value = 1;
  loadPlayers();
}, { deep: true });

watch(() => currentPage.value, () => {
  loadPlayers();
});

const resetFilters = () => {
  filters.value = {
    gender: '',
    utrMin: null,
    utrMax: null,
    ntrp: '',
    winRateMin: null,
    winRateMax: null,
    university: '',
    city: '',
    country: ''
  };
  simpleSearch.value = '';
};

const formatUTR = (stats) => {
  if (!stats) return '-';
  const rating = stats.utrRating ? stats.utrRating.toFixed(2) : '';
  const status = stats.utrStatus ? `(${stats.utrStatus})` : '';
  return `${rating} ${status}`.trim() || '-';
};

const formatNTRP = (stats) => {
  if (!stats) return '-';
  const rating = stats.ntrpRating ? stats.ntrpRating.toFixed(1) : '';
  const status = stats.ntrpStatus ? `(${stats.ntrpStatus})` : '';
  return `${rating} ${status}`.trim() || '-';
};

const editPlayer = (id) => {
  router.push(`/players/${id}/edit`);
};

const goToNewPlayer = () => {
  router.push('/players/new');
};

const clearSelection = () => {
  selectedPlayers.value = [];
};

const handleFileUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  try {
    const result = await playerService.importPlayersFromCSV(file);
    event.target.value = '';
    alert(result.message);
    await loadPlayers();
  } catch (err) {
    alert('Failed to import CSV: ' + (err.response?.data?.message || err.message));
    event.target.value = '';
  }
};

const exportToCSV = () => {
  const playersToExport = selectedPlayers.value.length > 0
    ? players.value.filter(p => selectedPlayers.value.includes(p.id))
    : players.value;

  const headers = ['Player ID', 'Name', 'Gender', 'UTR Rating', 'UTR Status', 'NTRP Rating', 'NTRP Status', 'Win Rate', 'City', 'Country'];

  const rows = playersToExport.map(player => {
    const utrRating = player.statistics?.utrRating ? player.statistics.utrRating.toFixed(2) : '-';
    const utrStatus = player.statistics?.utrStatus || '-';
    const ntrpRating = player.statistics?.ntrpRating ? player.statistics.ntrpRating.toFixed(1) : '-';
    const ntrpStatus = player.statistics?.ntrpStatus || '-';
    const winRate = player.statistics?.winRate !== null && player.statistics?.winRate !== undefined
      ? `${player.statistics.winRate.toFixed(1)}%`
      : '-';
    const gender = player.gender ? (player.gender.charAt(0).toUpperCase() + player.gender.slice(1)) : '-';

    return [
      player.id,
      player.name,
      gender,
      utrRating,
      utrStatus,
      ntrpRating,
      ntrpStatus,
      winRate,
      player.city || '-',
      player.country || '-'
    ];
  });

  const csvContent = [
    headers.join(','),
    ...rows.map(row => row.map(cell => {
      const cellStr = String(cell);
      if (cellStr.includes(',') || cellStr.includes('"') || cellStr.includes('\n')) {
        return `"${cellStr.replace(/"/g, '""')}"`;
      }
      return cellStr;
    }).join(','))
  ].join('\n');

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  const url = URL.createObjectURL(blob);

  link.setAttribute('href', url);
  link.setAttribute('download', `players_export_${new Date().toISOString().split('T')[0]}.csv`);
  link.style.visibility = 'hidden';

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

onMounted(() => {
  loadPlayers();
});
</script>
