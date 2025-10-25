<template>
  <div class="container mx-auto px-4 py-8 max-w-7xl">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-foreground">Players Management</h1>
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
        <Button
          @click="goToNewPlayer"
        >
          <Plus class="mr-2 h-4 w-4" />
          Add New Player
        </Button>
      </div>
    </div>

    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading players...</p>
    </div>

    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <Card v-else>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead class="text-center w-16">No.</TableHead>
            <TableHead class="text-center w-16">
              <input
                type="checkbox"
                @change="toggleSelectAll"
                :checked="selectedPlayers.length === playersWithStats.length && playersWithStats.length > 0"
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
            <TableHead>Dynamic Rating</TableHead>
            <TableHead>Win Rate</TableHead>
            <TableHead class="text-center">Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow v-for="(player, index) in playersWithStats" :key="player.id">
            <TableCell class="text-center text-muted-foreground">
              {{ index + 1 }}
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
              <div v-if="player.statistics?.dynamicRating">
                <a
                  v-if="player.statistics?.dynamicRatingUrl"
                  :href="player.statistics.dynamicRatingUrl"
                  target="_blank"
                  class="text-primary hover:underline flex items-center gap-1"
                >
                  {{ player.statistics.dynamicRating }}
                  <ExternalLink class="h-3 w-3" />
                </a>
                <span v-else>{{ player.statistics.dynamicRating }}</span>
              </div>
              <span v-else class="text-muted-foreground">-</span>
            </TableCell>
            <TableCell>
              <span v-if="player.statistics?.winRate !== null && player.statistics?.winRate !== undefined">
                {{ player.statistics.winRate.toFixed(1) }}%
              </span>
              <span v-else class="text-muted-foreground">-</span>
            </TableCell>
            <TableCell class="text-center">
              <div class="flex items-center justify-center gap-2">
                <Button
                  @click="editPlayer(player.id)"
                  variant="ghost"
                  size="icon"
                  title="Edit"
                >
                  <Pencil class="h-4 w-4" />
                </Button>
              </div>
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </Card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
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

const router = useRouter();
const players = ref([]);
const selectedPlayers = ref([]);
const loading = ref(true);
const error = ref(null);
const sortBy = ref(null);
const sortOrder = ref('desc');

const loadPlayers = async () => {
  try {
    loading.value = true;
    const playersData = await playerService.getAllPlayers();
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
      const aValue = a.gender || 'zzz';
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
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
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
    ? playersWithStats.value.filter(p => selectedPlayers.value.includes(p.id))
    : playersWithStats.value;

  const headers = ['Player ID', 'Name', 'Gender', 'UTR Rating', 'UTR Status', 'NTRP Rating', 'NTRP Status', 'Dynamic Rating', 'Win Rate'];

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
