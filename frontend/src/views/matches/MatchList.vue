<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Match Records</h1>
        <p class="text-muted-foreground mt-2">
          Manage and analyze match history and results
        </p>
      </div>
      <Button @click="openCreateModal">
        <Plus class="w-4 h-4 mr-2" />
        Add Match
      </Button>
    </div>

    <!-- Statistics Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4" v-if="statistics">
      <Card>
        <CardContent class="pt-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-muted-foreground">Total Matches</p>
              <p class="text-2xl font-bold">{{ statistics.totalMatches }}</p>
            </div>
            <Trophy class="w-8 h-8 text-muted-foreground" />
          </div>
        </CardContent>
      </Card>
      <Card>
        <CardContent class="pt-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-muted-foreground">Singles Matches</p>
              <p class="text-2xl font-bold">{{ statistics.singlesMatches }}</p>
            </div>
            <User class="w-8 h-8 text-muted-foreground" />
          </div>
        </CardContent>
      </Card>
      <Card>
        <CardContent class="pt-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-muted-foreground">Doubles Matches</p>
              <p class="text-2xl font-bold">{{ statistics.doublesMatches }}</p>
            </div>
            <Users class="w-8 h-8 text-muted-foreground" />
          </div>
        </CardContent>
      </Card>
    </div>

    <!-- Match List -->
    <Card v-if="matches.length > 0">
      <CardContent class="pt-6">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Date</TableHead>
              <TableHead>Type</TableHead>
              <TableHead>Tournament</TableHead>
              <TableHead>Round</TableHead>
              <TableHead>Score</TableHead>
              <TableHead>Result</TableHead>
              <TableHead>Winner</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="match in matches" :key="match.id">
              <TableCell>{{ formatDate(match.matchTime) }}</TableCell>
              <TableCell>
                <Badge>{{ match.matchType }}</Badge>
              </TableCell>
              <TableCell>{{ match.tournamentName || 'N/A' }}</TableCell>
              <TableCell>{{ match.round || 'N/A' }}</TableCell>
              <TableCell>{{ match.score || 'N/A' }}</TableCell>
              <TableCell>
                <Badge :variant="getResultVariant(match.result)">
                  {{ match.result }}
                </Badge>
              </TableCell>
              <TableCell>{{ getWinnerNames(match) }}</TableCell>
              <TableCell>
                <div class="flex gap-2">
                  <Button
                    size="sm"
                    :variant="matchVideos[match.id] ? 'default' : 'outline'"
                    @click="handleVideoAction(match)"
                    :title="matchVideos[match.id] ? 'Watch Video' : 'Add Video'"
                  >
                    <Video class="w-4 h-4" />
                  </Button>
                  <Button size="sm" variant="outline" @click="openEditModal(match)">
                    <Edit2 class="w-4 h-4" />
                  </Button>
                  <Button size="sm" variant="destructive" @click="deleteMatch(match.id)">
                    <Trash2 class="w-4 h-4" />
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </CardContent>
    </Card>

    <!-- Empty State -->
    <Card v-else-if="!loading">
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <Trophy class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">No Matches Yet</h3>
          <p class="text-muted-foreground mb-6">
            Start by adding your first match record
          </p>
          <Button @click="openCreateModal">
            <Plus class="w-4 h-4 mr-2" />
            Add First Match
          </Button>
        </div>
      </CardContent>
    </Card>

    <!-- Loading State -->
    <Card v-if="loading">
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <p class="text-muted-foreground">Loading matches...</p>
        </div>
      </CardContent>
    </Card>

    <!-- Create/Edit Match Modal -->
    <MatchFormModal
      v-if="showModal"
      :match="selectedMatch"
      @close="closeModal"
      @save="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import Button from '../../components/ui/Button.vue';
import Badge from '../../components/ui/Badge.vue';
import Table from '../../components/ui/Table.vue';
import TableHeader from '../../components/ui/TableHeader.vue';
import TableBody from '../../components/ui/TableBody.vue';
import TableRow from '../../components/ui/TableRow.vue';
import TableHead from '../../components/ui/TableHead.vue';
import TableCell from '../../components/ui/TableCell.vue';
import { Trophy, Plus, Edit2, Trash2, User, Users, Video } from 'lucide-vue-next';
import matchService from '../../services/matchService';
import videoAnalysisService from '../../services/videoAnalysisService';
import MatchFormModal from '../../components/MatchFormModal.vue';

const router = useRouter();
const matches = ref([]);
const matchVideos = ref({}); // Map of matchId -> video
const statistics = ref(null);
const loading = ref(false);
const showModal = ref(false);
const selectedMatch = ref(null);

const loadMatches = async () => {
  loading.value = true;
  try {
    matches.value = await matchService.getAllMatches();
    statistics.value = await matchService.getMatchStatistics();

    // Load videos for each match
    matchVideos.value = {};
    for (const match of matches.value) {
      try {
        const videos = await videoAnalysisService.getVideosByMatch(match.id);
        if (videos.length > 0) {
          matchVideos.value[match.id] = videos[0]; // One video per match
        }
      } catch (error) {
        console.error(`Error loading video for match ${match.id}:`, error);
      }
    }
  } catch (error) {
    console.error('Error loading matches:', error);
    alert('Failed to load matches');
  } finally {
    loading.value = false;
  }
};

const openCreateModal = () => {
  selectedMatch.value = null;
  showModal.value = true;
};

const openEditModal = (match) => {
  selectedMatch.value = match;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  selectedMatch.value = null;
};

const handleSave = async () => {
  closeModal();
  await loadMatches();
};

const deleteMatch = async (id) => {
  if (!confirm('Are you sure you want to delete this match?')) {
    return;
  }

  try {
    await matchService.deleteMatch(id);
    await loadMatches();
  } catch (error) {
    console.error('Error deleting match:', error);
    alert('Failed to delete match');
  }
};

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const getResultVariant = (result) => {
  switch (result) {
    case 'complete':
      return 'default';
    case 'retired':
      return 'secondary';
    case 'default':
    case 'double_default':
      return 'destructive';
    default:
      return 'default';
  }
};

const handleVideoAction = (match) => {
  const video = matchVideos.value[match.id];
  if (video && video.videoUrl) {
    // Open the video in a new tab
    window.open(video.videoUrl, '_blank');
  } else {
    // No video exists, open edit modal to add one
    openEditModal(match);
  }
};

const getWinnerNames = (match) => {
  if (!match.winnerSide) return 'N/A';

  const winners = [];
  if (match.winnerSide === 'team1') {
    if (match.player1Name) winners.push(match.player1Name);
    if (match.player2Name) winners.push(match.player2Name);
  } else if (match.winnerSide === 'team2') {
    if (match.opponentPlayer1Name) winners.push(match.opponentPlayer1Name);
    if (match.opponentPlayer2Name) winners.push(match.opponentPlayer2Name);
  }

  return winners.length > 0 ? winners.join(' / ') : match.winnerSide;
};

onMounted(() => {
  loadMatches();
});
</script>
