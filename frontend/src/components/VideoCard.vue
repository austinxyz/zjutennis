<template>
  <Card class="overflow-hidden hover:shadow-lg transition-shadow cursor-pointer" @click="$emit('select', video)">
    <!-- Thumbnail -->
    <div class="relative h-48 bg-gray-200 overflow-hidden">
      <img
        v-if="video.thumbnailUrl"
        :src="video.thumbnailUrl"
        :alt="video.title"
        class="w-full h-full object-cover"
      />
      <div v-else class="w-full h-full flex items-center justify-center bg-gradient-to-br from-blue-500 to-purple-600">
        <Video class="w-16 h-16 text-white opacity-50" />
      </div>

      <!-- Status Badge -->
      <div class="absolute top-2 right-2">
        <Badge :variant="getStatusVariant(video.status)">
          {{ video.status }}
        </Badge>
      </div>

      <!-- AI Analyzed Badge -->
      <div v-if="video.aiAnalyzed" class="absolute top-2 left-2">
        <Badge variant="success">AI Analyzed</Badge>
      </div>

      <!-- Duration -->
      <div v-if="video.durationSeconds" class="absolute bottom-2 right-2 bg-black bg-opacity-75 text-white text-xs px-2 py-1 rounded">
        {{ formatDuration(video.durationSeconds) }}
      </div>
    </div>

    <!-- Content -->
    <div class="p-4">
      <h3 class="font-semibold text-lg mb-1 truncate">{{ video.title }}</h3>

      <div class="flex items-center gap-2 text-sm text-muted-foreground mb-2">
        <Calendar class="w-4 h-4" />
        <span>{{ formatDate(video.matchDate) }}</span>
      </div>

      <!-- Player Information -->
      <div v-if="video.player" class="flex items-center gap-2 text-sm mb-2">
        <Badge variant="outline">
          {{ video.player.name }}
        </Badge>
      </div>

      <p v-if="video.description" class="text-sm text-muted-foreground line-clamp-2 mb-3">
        {{ video.description }}
      </p>

      <!-- Match Statistics Summary -->
      <div v-if="hasStats" class="grid grid-cols-3 gap-2 text-xs text-center border-t pt-3">
        <div v-if="video.totalShots">
          <div class="font-semibold text-blue-600">{{ video.totalShots }}</div>
          <div class="text-muted-foreground">Shots</div>
        </div>
        <div v-if="video.winners">
          <div class="font-semibold text-green-600">{{ video.winners }}</div>
          <div class="text-muted-foreground">Winners</div>
        </div>
        <div v-if="video.errors">
          <div class="font-semibold text-red-600">{{ video.errors }}</div>
          <div class="text-muted-foreground">Errors</div>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex gap-2 mt-3 pt-3 border-t">
        <Button
          v-if="video.videoUrl"
          size="sm"
          variant="outline"
          @click.stop="openVideo"
          class="flex-1"
        >
          <ExternalLink class="w-3 h-3 mr-1" />
          Watch
        </Button>
        <Button
          size="sm"
          variant="outline"
          @click.stop="$emit('edit', video)"
          class="flex-1"
        >
          <Edit class="w-3 h-3 mr-1" />
          {{ video.player ? `Analyze: ${video.player.name}` : 'Edit' }}
        </Button>
        <Button
          size="sm"
          variant="destructive"
          @click.stop="$emit('delete', video)"
        >
          <Trash2 class="w-3 h-3" />
        </Button>
      </div>
    </div>
  </Card>
</template>

<script setup>
import { computed } from 'vue';
import { Calendar, Video, ExternalLink, Edit, Trash2 } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import Badge from './ui/Badge.vue';
import Button from './ui/Button.vue';

const props = defineProps({
  video: {
    type: Object,
    required: true
  }
});

defineEmits(['select', 'edit', 'delete']);

const hasStats = computed(() => {
  return props.video.totalShots || props.video.winners || props.video.errors;
});

const getStatusVariant = (status) => {
  const variants = {
    'pending': 'secondary',
    'processing': 'warning',
    'completed': 'success',
    'failed': 'destructive'
  };
  return variants[status] || 'default';
};

const formatDuration = (seconds) => {
  if (!seconds) return '';
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;

  if (hours > 0) {
    return `${hours}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  }
  return `${minutes}:${String(secs).padStart(2, '0')}`;
};

const formatDate = (dateString) => {
  if (!dateString) return 'No date';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const openVideo = () => {
  if (props.video.videoUrl) {
    window.open(props.video.videoUrl, '_blank');
  }
};
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
