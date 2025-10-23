<template>
  <div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-foreground">Players Management</h1>
      <div class="flex gap-3">
        <button
          v-if="selectedPlayers.length > 0"
          @click="clearSelection"
          class="px-4 py-2 border border-muted-foreground text-muted-foreground rounded-md hover:bg-accent"
        >
          Clear Selection ({{ selectedPlayers.length }})
        </button>
        <button
          @click="exportToCSV"
          class="px-4 py-2 border border-primary text-primary rounded-md hover:bg-primary/10"
        >
          Export {{ selectedPlayers.length > 0 ? `Selected (${selectedPlayers.length})` : 'All' }}
        </button>
        <label
          class="px-4 py-2 border border-primary text-primary rounded-md hover:bg-primary/10 cursor-pointer"
        >
          Import CSV
          <input
            type="file"
            ref="fileInput"
            @change="handleFileUpload"
            accept=".csv"
            class="hidden"
          />
        </label>
        <button
          @click="goToNewPlayer"
          class="px-4 py-2 bg-primary text-primary-foreground rounded-md hover:bg-primary/90"
        >
          Add New Player
        </button>
      </div>
    </div>

    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading players...</p>
    </div>

    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <div v-else class="bg-card rounded-lg shadow overflow-x-auto">
      <table class="min-w-full divide-y divide-border">
        <thead class="bg-muted">
          <tr>
            <th class="px-3 py-3 text-center text-xs font-medium text-muted-foreground uppercase tracking-wider">
              No.
            </th>
            <th class="px-3 py-3 text-center text-xs font-medium text-muted-foreground uppercase tracking-wider">
              <input
                type="checkbox"
                @change="toggleSelectAll"
                :checked="selectedPlayers.length === playersWithStats.length && playersWithStats.length > 0"
                class="h-4 w-4 rounded border-input"
              />
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
              Name
            </th>
            <th
              @click="toggleSort('gender')"
              class="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:bg-muted/50"
            >
              <div class="flex items-center gap-1">
                <span>Gender</span>
                <svg v-if="sortBy === 'gender'" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                  <path v-if="sortOrder === 'asc'" fill-rule="evenodd" d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z" clip-rule="evenodd" />
                  <path v-else fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
              </div>
            </th>
            <th
              @click="toggleSort('utr')"
              class="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:bg-muted/50"
            >
              <div class="flex items-center gap-1">
                <span>UTR</span>
                <svg v-if="sortBy === 'utr'" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                  <path v-if="sortOrder === 'asc'" fill-rule="evenodd" d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z" clip-rule="evenodd" />
                  <path v-else fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
              </div>
            </th>
            <th
              @click="toggleSort('ntrp')"
              class="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider cursor-pointer hover:bg-muted/50"
            >
              <div class="flex items-center gap-1">
                <span>NTRP</span>
                <svg v-if="sortBy === 'ntrp'" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                  <path v-if="sortOrder === 'asc'" fill-rule="evenodd" d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z" clip-rule="evenodd" />
                  <path v-else fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
              </div>
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
              Dynamic Rating
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
              Win Rate
            </th>
            <th class="px-6 py-3 text-center text-xs font-medium text-muted-foreground uppercase tracking-wider">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="bg-card divide-y divide-border">
          <tr v-for="(player, index) in playersWithStats" :key="player.id" class="hover:bg-accent/50">
            <td class="px-3 py-4 whitespace-nowrap text-center text-sm text-muted-foreground">
              {{ index + 1 }}
            </td>
            <td class="px-3 py-4 whitespace-nowrap text-center">
              <input
                type="checkbox"
                :value="player.id"
                v-model="selectedPlayers"
                class="h-4 w-4 rounded border-input"
              />
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-foreground">
              {{ player.name }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-muted-foreground">
              {{ player.gender ? (player.gender.charAt(0).toUpperCase() + player.gender.slice(1)) : '-' }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-foreground">
              <div v-if="player.statistics?.utrRating || player.statistics?.utrStatus">
                <a
                  v-if="player.statistics?.utrUrl"
                  :href="player.statistics.utrUrl"
                  target="_blank"
                  class="text-primary hover:underline"
                >
                  {{ formatUTR(player.statistics) }}
                </a>
                <span v-else>{{ formatUTR(player.statistics) }}</span>
              </div>
              <span v-else class="text-muted-foreground">-</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-foreground">
              <div v-if="player.statistics?.ntrpRating || player.statistics?.ntrpStatus">
                <a
                  v-if="player.statistics?.ntrpUrl"
                  :href="player.statistics.ntrpUrl"
                  target="_blank"
                  class="text-primary hover:underline"
                >
                  {{ formatNTRP(player.statistics) }}
                </a>
                <span v-else>{{ formatNTRP(player.statistics) }}</span>
              </div>
              <span v-else class="text-muted-foreground">-</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-foreground">
              <div v-if="player.statistics?.dynamicRating">
                <a
                  v-if="player.statistics?.dynamicRatingUrl"
                  :href="player.statistics.dynamicRatingUrl"
                  target="_blank"
                  class="text-primary hover:underline"
                >
                  {{ player.statistics.dynamicRating }}
                </a>
                <span v-else>{{ player.statistics.dynamicRating }}</span>
              </div>
              <span v-else class="text-muted-foreground">-</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-foreground">
              <span v-if="player.statistics?.winRate !== null && player.statistics?.winRate !== undefined">
                {{ player.statistics.winRate.toFixed(1) }}%
              </span>
              <span v-else class="text-muted-foreground">-</span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-center">
              <button
                @click="editPlayer(player.id)"
                class="text-primary hover:text-primary/80 mr-3"
                title="Edit"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                </svg>
              </button>
              <button
                @click="confirmDelete(player)"
                class="text-destructive hover:text-destructive/80"
                title="Delete"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import playerService from '../services/playerService';

const router = useRouter();
const players = ref([]);
const selectedPlayers = ref([]);
const loading = ref(true);
const error = ref(null);
const sortBy = ref(null); // 'utr', 'ntrp', or 'gender'
const sortOrder = ref('desc'); // 'asc' or 'desc'

const loadPlayers = async () => {
  try {
    loading.value = true;
    const playersData = await playerService.getAllPlayers();

    // Players now include statistics from the unified entity
    players.value = playersData;
  } catch (err) {
    error.value = 'Failed to load players: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const playersWithStats = computed(() => {
  let sorted = [...players.value];

  if (sortBy.value === 'utr') {
    sorted.sort((a, b) => {
      const aValue = a.statistics?.utrRating ?? -1;
      const bValue = b.statistics?.utrRating ?? -1;
      return sortOrder.value === 'asc' ? aValue - bValue : bValue - aValue;
    });
  } else if (sortBy.value === 'ntrp') {
    sorted.sort((a, b) => {
      const aValue = a.statistics?.ntrpRating ?? -1;
      const bValue = b.statistics?.ntrpRating ?? -1;
      return sortOrder.value === 'asc' ? aValue - bValue : bValue - aValue;
    });
  } else if (sortBy.value === 'gender') {
    sorted.sort((a, b) => {
      const aValue = a.gender || 'zzz'; // Empty values go to end
      const bValue = b.gender || 'zzz';
      return sortOrder.value === 'asc'
        ? aValue.localeCompare(bValue)
        : bValue.localeCompare(aValue);
    });
  }

  return sorted;
});

const toggleSort = (column) => {
  if (sortBy.value === column) {
    // Toggle sort order
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    // New column, default to descending
    sortBy.value = column;
    sortOrder.value = 'desc';
  }
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

const confirmDelete = async (player) => {
  if (confirm(`Are you sure you want to delete ${player.name}?`)) {
    try {
      await playerService.deletePlayer(player.id);
      await loadPlayers();
    } catch (err) {
      alert('Failed to delete player: ' + err.message);
    }
  }
};

const toggleSelectAll = (event) => {
  if (event.target.checked) {
    selectedPlayers.value = playersWithStats.value.map(p => p.id);
  } else {
    selectedPlayers.value = [];
  }
};

const clearSelection = () => {
  selectedPlayers.value = [];
};

const handleFileUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  try {
    const result = await playerService.importPlayersFromCSV(file);

    // Reset file input
    event.target.value = '';

    // Show result message
    alert(result.message);

    // Reload the player list
    await loadPlayers();
  } catch (err) {
    alert('Failed to import CSV: ' + (err.response?.data?.message || err.message));
    event.target.value = '';
  }
};

const exportToCSV = () => {
  // Determine which players to export
  const playersToExport = selectedPlayers.value.length > 0
    ? playersWithStats.value.filter(p => selectedPlayers.value.includes(p.id))
    : playersWithStats.value;

  // CSV headers with separated fields
  const headers = ['Player ID', 'Name', 'Gender', 'UTR Rating', 'UTR Status', 'NTRP Rating', 'NTRP Status', 'Dynamic Rating', 'Win Rate'];

  // CSV rows
  const rows = playersToExport.map(player => {
    const utrRating = player.statistics?.utrRating ? player.statistics.utrRating.toFixed(2) : '-';
    const utrStatus = player.statistics?.utrStatus || '-';
    const ntrpRating = player.statistics?.ntrpRating ? player.statistics.ntrpRating.toFixed(1) : '-';
    const ntrpStatus = player.statistics?.ntrpStatus || '-';
    const dynamicRating = player.statistics?.dynamicRating || '-';
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
      dynamicRating,
      winRate
    ];
  });

  // Combine headers and rows
  const csvContent = [
    headers.join(','),
    ...rows.map(row => row.map(cell => {
      // Escape cells that contain commas or quotes
      const cellStr = String(cell);
      if (cellStr.includes(',') || cellStr.includes('"') || cellStr.includes('\n')) {
        return `"${cellStr.replace(/"/g, '""')}"`;
      }
      return cellStr;
    }).join(','))
  ].join('\n');

  // Create blob and download
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
