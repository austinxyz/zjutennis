<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4" @click.self="$emit('close')">
    <Card class="w-full max-w-4xl max-h-[90vh] overflow-y-auto">
      <!-- Header -->
      <div class="p-6 border-b flex justify-between items-start sticky top-0 bg-white z-10">
        <div>
          <h2 class="text-2xl font-bold">{{ video.title }}</h2>
          <p class="text-sm text-muted-foreground mt-1">
            {{ formatDate(video.matchDate) }}
          </p>
        </div>
        <Button variant="ghost" size="sm" @click="$emit('close')">
          <X class="h-5 h-5" />
        </Button>
      </div>

      <div class="p-6">
        <!-- Video Embed (if available) -->
        <div v-if="video.videoUrl" class="mb-6">
          <div class="aspect-video bg-gray-200 rounded-lg overflow-hidden">
            <iframe
              v-if="getEmbedUrl(video.videoUrl)"
              :src="getEmbedUrl(video.videoUrl)"
              class="w-full h-full"
              frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen
            ></iframe>
            <div v-else class="w-full h-full flex items-center justify-center">
              <Button @click="openVideo">
                <ExternalLink class="mr-2 h-4 w-4" />
                Open Video
              </Button>
            </div>
          </div>
        </div>

        <!-- Video Information -->
        <div class="space-y-4">
          <div v-if="video.description">
            <h3 class="font-semibold text-lg mb-2">Description</h3>
            <p class="text-sm text-muted-foreground">{{ video.description }}</p>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div v-if="video.durationSeconds">
              <h4 class="text-sm font-medium text-muted-foreground">Duration</h4>
              <p class="text-sm">{{ formatDuration(video.durationSeconds) }}</p>
            </div>
            <div v-if="video.matchDate">
              <h4 class="text-sm font-medium text-muted-foreground">Date</h4>
              <p class="text-sm">{{ formatDate(video.matchDate) }}</p>
            </div>
          </div>

          <div v-if="video.videoUrl">
            <h4 class="text-sm font-medium text-muted-foreground mb-2">Video URL</h4>
            <a
              :href="video.videoUrl"
              target="_blank"
              class="text-sm text-blue-600 hover:underline flex items-center"
            >
              {{ video.videoUrl }}
              <ExternalLink class="ml-1 h-3 w-3" />
            </a>
          </div>
        </div>
      </div>
    </Card>
  </div>
</template>

<script setup>
import { X, ExternalLink } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import Button from './ui/Button.vue';

const props = defineProps({
  video: {
    type: Object,
    required: true
  }
});

defineEmits(['close', 'updated']);

const formatDate = (dateString) => {
  if (!dateString) return 'No date';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const formatDuration = (seconds) => {
  if (!seconds) return 'N/A';
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
};

const getEmbedUrl = (url) => {
  if (!url) return null;

  // YouTube
  const youtubeMatch = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([^&\s]+)/);
  if (youtubeMatch) {
    return `https://www.youtube.com/embed/${youtubeMatch[1]}`;
  }

  // Vimeo
  const vimeoMatch = url.match(/vimeo\.com\/(\d+)/);
  if (vimeoMatch) {
    return `https://player.vimeo.com/video/${vimeoMatch[1]}`;
  }

  return null;
};

const openVideo = () => {
  if (props.video.videoUrl) {
    window.open(props.video.videoUrl, '_blank');
  }
};
</script>
