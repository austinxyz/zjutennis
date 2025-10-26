<template>
  <div class="h-full flex flex-col">
    <!-- Header -->
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

    <!-- Main Content Area -->
    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading players...</p>
    </div>

    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <div v-else class="flex gap-6 flex-1 overflow-hidden">
      <!-- Left Sidebar - Search Filters -->
      <Card class="w-80 flex-shrink-0 overflow-y-auto">
        <div class="p-6 space-y-6">
          <div>
            <h2 class="text-lg font-semibold mb-4">Search Filters</h2>
            <Button
              @click="resetFilters"
              variant="outline"
              size="sm"
              class="w-full mb-4"
            >
              Reset All Filters
            </Button>
          </div>

          <!-- Name Search -->
          <div class="space-y-2">
            <Label for="name">Name</Label>
            <Input
              id="name"
              v-model="filters.name"
              placeholder="Search by name..."
              type="text"
            />
          </div>

          <!-- Gender Filter -->
          <div class="space-y-2">
            <Label for="gender">Gender</Label>
            <select
              id="gender"
              v-model="filters.gender"
              class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
            >
              <option value="">All</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
            </select>
          </div>

          <!-- UTR Range -->
          <div class="space-y-2">
            <Label>UTR Range</Label>
            <div class="grid grid-cols-2 gap-2">
              <Input
                v-model.number="filters.utrMin"
                placeholder="Min"
                type="number"
                step="0.1"
              />
              <Input
                v-model.number="filters.utrMax"
                placeholder="Max"
                type="number"
                step="0.1"
              />
            </div>
          </div>

          <!-- NTRP Filter -->
          <div class="space-y-2">
            <Label for="ntrp">NTRP Level</Label>
            <select
              id="ntrp"
              v-model="filters.ntrp"
              class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
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

          <!-- Win Rate Range -->
          <div class="space-y-2">
            <Label>Win Rate (%)</Label>
            <div class="grid grid-cols-2 gap-2">
              <Input
                v-model.number="filters.winRateMin"
                placeholder="Min"
                type="number"
                step="1"
                min="0"
                max="100"
              />
              <Input
                v-model.number="filters.winRateMax"
                placeholder="Max"
                type="number"
                step="1"
                min="0"
                max="100"
              />
            </div>
          </div>

          <!-- Graduation University -->
          <div class="space-y-2">
            <Label for="university">Graduation University</Label>
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
        </div>
      </Card>

      <!-- Right Side - Results -->
      <div class="flex-1 flex flex-col overflow-hidden">
        <Card class="flex-1 flex flex-col overflow-hidden">
          <div class="overflow-x-auto flex-1">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead class="text-center w-16">No.</TableHead>
                  <TableHead class="text-center w-16">
                    <input
                      type="checkbox"
                      @change="toggleSelectAll"
                      :checked="selectedPlayers.length === paginatedPlayers.length && paginatedPlayers.length > 0"
                      class="h-4 w-4 rounded border-input"
                    />
                  </TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead
                    @click="toggleSort('gender')"
                    class="cursor-pointer hover:bg-muted/50"
                  >
                    <div class="flex items-center gap-1">
                      <span>Gender</span>
                      <ArrowUpDown v-if="sortBy !== 'gender'" class="h-4 w-4 opacity-50" />
                      <ChevronUp v-else-if="sortOrder === 'asc'" class="h-4 w-4" />
                      <ChevronDown v-else class="h-4 w-4" />
                    </div>
                  </TableHead>
                  <TableHead
                    @click="toggleSort('utr')"
                    class="cursor-pointer hover:bg-muted/50"
                  >
                    <div class="flex items-center gap-1">
                      <span>UTR</span>
                      <ArrowUpDown v-if="sortBy !== 'utr'" class="h-4 w-4 opacity-50" />
                      <ChevronUp v-else-if="sortOrder === 'asc'" class="h-4 w-4" />
                      <ChevronDown v-else class="h-4 w-4" />
                    </div>
                  </TableHead>
                  <TableHead
                    @click="toggleSort('ntrp')"
                    class="cursor-pointer hover:bg-muted/50"
                  >
                    <div class="flex items-center gap-1">
                      <span>NTRP</span>
                      <ArrowUpDown v-if="sortBy !== 'ntrp'" class="h-4 w-4 opacity-50" />
                      <ChevronUp v-else-if="sortOrder === 'asc'" class="h-4 w-4" />
                      <ChevronDown v-else class="h-4 w-4" />
                    </div>
                  </TableHead>
                  <TableHead>Win Rate</TableHead>
                  <TableHead>City</TableHead>
                  <TableHead>University</TableHead>
                  <TableHead class="text-center">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                <TableRow v-if="players.length === 0">
                  <TableCell :colspan="10" class="text-center py-8 text-muted-foreground">
                    No players found matching your criteria
                  </TableCell>
                </TableRow>
                <TableRow v-for="(player, index) in paginatedPlayers" :key="player.id">
                  <TableCell class="text-center text-muted-foreground">
                    {{ (currentPage - 1) * pageSize + index + 1 }}
                  </TableCell>
                  <TableCell class="text-center">
                    <input
                      type="checkbox"
                      :value="player.id"
                      v-model="selectedPlayers"
                      class="h-4 w-4 rounded border-input"
                    />
                  </TableCell>
                  <TableCell class="font-medium">{{ player.name }}</TableCell>
                  <TableCell>
                    <Badge v-if="player.gender" variant="outline">
                      {{ player.gender.charAt(0).toUpperCase() + player.gender.slice(1) }}
                    </Badge>
                    <span v-else class="text-muted-foreground">-</span>
                  </TableCell>
                  <TableCell>
                    <div v-if="player.statistics?.utrRating || player.statistics?.utrStatus">
                      <a
                        v-if="player.statistics?.utrUrl"
                        :href="player.statistics.utrUrl"
                        target="_blank"
                        class="text-primary hover:underline flex items-center gap-1"
                      >
                        {{ formatUTR(player.statistics) }}
                        <ExternalLink class="h-3 w-3" />
                      </a>
                      <span v-else>{{ formatUTR(player.statistics) }}</span>
                    </div>
                    <span v-else class="text-muted-foreground">-</span>
                  </TableCell>
                  <TableCell>
                    <div v-if="player.statistics?.ntrpRating || player.statistics?.ntrpStatus">
                      <a
                        v-if="player.statistics?.ntrpUrl"
                        :href="player.statistics.ntrpUrl"
                        target="_blank"
                        class="text-primary hover:underline flex items-center gap-1"
                      >
                        {{ formatNTRP(player.statistics) }}
                        <ExternalLink class="h-3 w-3" />
                      </a>
                      <span v-else>{{ formatNTRP(player.statistics) }}</span>
                    </div>
                    <span v-else class="text-muted-foreground">-</span>
                  </TableCell>
                  <TableCell>
                    <span v-if="player.statistics?.winRate !== null && player.statistics?.winRate !== undefined">
                      {{ player.statistics.winRate.toFixed(1) }}%
                    </span>
                    <span v-else class="text-muted-foreground">-</span>
                  </TableCell>
                  <TableCell>
                    <span v-if="player.city">{{ player.city }}</span>
                    <span v-else class="text-muted-foreground">-</span>
                  </TableCell>
                  <TableCell>
                    <span v-if="getUniversity(player)">{{ getUniversity(player) }}</span>
                    <span v-else class="text-muted-foreground">-</span>
                  </TableCell>
                  <TableCell class="text-center">
                    <Button
                      @click="editPlayer(player.id)"
                      variant="ghost"
                      size="icon"
                      title="Edit"
                    >
                      <Pencil class="h-4 w-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </div>
        </Card>

        <!-- Pagination -->
        <div v-if="totalPages > 1" class="flex items-center justify-between mt-4">
          <div class="text-sm text-muted-foreground">
            Showing {{ (currentPage - 1) * pageSize + 1 }} to {{ Math.min(currentPage * pageSize, totalCount) }} of {{ totalCount }} results
          </div>
          <div class="flex items-center gap-2">
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
              Previous
            </Button>
            <div class="flex items-center gap-1">
              <template v-for="page in visiblePages" :key="page">
                <Button
                  v-if="page !== '...'"
                  @click="currentPage = page"
                  :variant="currentPage === page ? 'default' : 'outline'"
                  size="sm"
                  class="w-10"
                >
                  {{ page }}
                </Button>
                <span v-else class="px-2">...</span>
              </template>
            </div>
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
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { Plus, Pencil, ExternalLink, ArrowUpDown, ChevronUp, ChevronDown } from 'lucide-vue-next';
import playerService from '../services/playerService';
import Button from '../components/ui/Button.vue';
import Card from '../components/ui/Card.vue';
import Table from '../components/ui/Table.vue';
import TableHeader from '../components/ui/TableHeader.vue';
import TableBody from '../components/ui/TableBody.vue';
import TableRow from '../components/ui/TableRow.vue';
import TableHead from '../components/ui/TableHead.vue';
import TableCell from '../components/ui/TableCell.vue';
import Badge from '../components/ui/Badge.vue';
import Label from '../components/ui/Label.vue';
import Input from '../components/ui/Input.vue';

const router = useRouter();
const players = ref([]);
const selectedPlayers = ref([]);
const loading = ref(true);
const error = ref(null);
const sortBy = ref(null);
const sortOrder = ref('desc');
const currentPage = ref(1);
const pageSize = 25;
const totalCount = ref(0);
const totalPages = ref(0);

// Search filters with default to Zhejiang University
const filters = ref({
  name: '',
  gender: '',
  utrMin: null,
  utrMax: null,
  ntrp: '',
  winRateMin: null,
  winRateMax: null,
  university: 'Zhejiang University',
  city: '',
  country: ''
});

const loadPlayers = async () => {
  try {
    loading.value = true;
    const searchRequest = {
      name: filters.value.name || null,
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
      sortBy: sortBy.value,
      sortOrder: sortOrder.value
    };
    const response = await playerService.searchPlayers(searchRequest);
    players.value = response.players;
    totalCount.value = response.totalCount;
    totalPages.value = response.totalPages;
  } catch (err) {
    error.value = 'Failed to load players: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const getUniversity = (player) => {
  if (player.alumni?.graduationUniversity1) {
    return player.alumni.graduationUniversity1;
  }
  return '';
};

// Filtered players is now just the players from the backend
const filteredPlayers = computed(() => {
  return players.value;
});

// Paginated players is also just the players from the backend (already paginated)
const paginatedPlayers = computed(() => {
  return players.value;
});

const visiblePages = computed(() => {
  const pages = [];
  const total = totalPages.value;
  const current = currentPage.value;

  if (total <= 7) {
    for (let i = 1; i <= total; i++) {
      pages.push(i);
    }
  } else {
    if (current <= 4) {
      for (let i = 1; i <= 5; i++) pages.push(i);
      pages.push('...');
      pages.push(total);
    } else if (current >= total - 3) {
      pages.push(1);
      pages.push('...');
      for (let i = total - 4; i <= total; i++) pages.push(i);
    } else {
      pages.push(1);
      pages.push('...');
      for (let i = current - 1; i <= current + 1; i++) pages.push(i);
      pages.push('...');
      pages.push(total);
    }
  }

  return pages;
});

// Reload when filters change
watch(() => filters.value, () => {
  currentPage.value = 1;
  loadPlayers();
}, { deep: true });

// Reload when page changes
watch(() => currentPage.value, () => {
  loadPlayers();
});

const toggleSort = (column) => {
  if (sortBy.value === column) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortBy.value = column;
    sortOrder.value = 'desc';
  }
  loadPlayers();
};

const resetFilters = () => {
  filters.value = {
    name: '',
    gender: '',
    utrMin: null,
    utrMax: null,
    ntrp: '',
    winRateMin: null,
    winRateMax: null,
    university: 'Zhejiang University',
    city: '',
    country: ''
  };
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

const toggleSelectAll = (event) => {
  if (event.target.checked) {
    selectedPlayers.value = paginatedPlayers.value.map(p => p.id);
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

  const headers = ['Player ID', 'Name', 'Gender', 'UTR Rating', 'UTR Status', 'NTRP Rating', 'NTRP Status', 'Win Rate', 'City', 'Country', 'University'];

  const rows = playersToExport.map(player => {
    const utrRating = player.statistics?.utrRating ? player.statistics.utrRating.toFixed(2) : '-';
    const utrStatus = player.statistics?.utrStatus || '-';
    const ntrpRating = player.statistics?.ntrpRating ? player.statistics.ntrpRating.toFixed(1) : '-';
    const ntrpStatus = player.statistics?.ntrpStatus || '-';
    const winRate = player.statistics?.winRate !== null && player.statistics?.winRate !== undefined
      ? `${player.statistics.winRate.toFixed(1)}%`
      : '-';
    const gender = player.gender ? (player.gender.charAt(0).toUpperCase() + player.gender.slice(1)) : '-';
    const university = getUniversity(player) || '-';

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
      player.country || '-',
      university
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
