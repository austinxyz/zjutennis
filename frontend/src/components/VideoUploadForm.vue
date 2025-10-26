<template>
  <Card class="p-6">
    <h3 class="text-lg font-semibold mb-4">{{ isEdit ? 'Edit' : 'Add' }} Video Analysis</h3>

    <form @submit.prevent="handleSubmit">
      <!-- Basic Information -->
      <div class="space-y-4">
        <div>
          <Label for="title">Video Title *</Label>
          <Input
            id="title"
            v-model="formData.title"
            placeholder="e.g., Singles Match vs John Doe - Oct 2025"
            required
          />
        </div>

        <div>
          <Label for="description">Description</Label>
          <Textarea
            id="description"
            v-model="formData.description"
            :rows="3"
            placeholder="Brief description of the match or training session..."
          />
        </div>

        <div>
          <Label for="videoUrl">Video URL (YouTube, Vimeo, etc.)</Label>
          <Input
            id="videoUrl"
            v-model="formData.videoUrl"
            type="url"
            placeholder="https://youtube.com/watch?v=..."
          />
        </div>

        <div>
          <Label for="thumbnailUrl">Thumbnail URL (optional)</Label>
          <Input
            id="thumbnailUrl"
            v-model="formData.thumbnailUrl"
            type="url"
            placeholder="https://..."
          />
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <Label for="matchDate">Match Date</Label>
            <Input
              id="matchDate"
              v-model="formData.matchDate"
              type="date"
            />
          </div>
          <div>
            <Label for="durationSeconds">Duration (seconds)</Label>
            <Input
              id="durationSeconds"
              v-model.number="formData.durationSeconds"
              type="number"
              placeholder="e.g., 3600"
            />
          </div>
        </div>
      </div>

      <!-- Match Statistics -->
      <div class="mt-6">
        <h4 class="font-semibold mb-3">Match Statistics (optional)</h4>
        <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
          <div>
            <Label for="totalShots">Total Shots</Label>
            <Input
              id="totalShots"
              v-model.number="formData.totalShots"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="winners">Winners</Label>
            <Input
              id="winners"
              v-model.number="formData.winners"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="errors">Errors</Label>
            <Input
              id="errors"
              v-model.number="formData.errors"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="aces">Aces</Label>
            <Input
              id="aces"
              v-model.number="formData.aces"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="doubleFaults">Double Faults</Label>
            <Input
              id="doubleFaults"
              v-model.number="formData.doubleFaults"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="runningDistance">Running Distance (m)</Label>
            <Input
              id="runningDistance"
              v-model.number="formData.runningDistanceMeters"
              type="number"
              step="0.1"
              placeholder="0.0"
            />
          </div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="flex gap-3 mt-6">
        <Button type="submit" :disabled="saving">
          {{ saving ? 'Saving...' : (isEdit ? 'Update' : 'Create') }}
        </Button>
        <Button type="button" variant="outline" @click="$emit('cancel')">
          Cancel
        </Button>
      </div>
    </form>
  </Card>
</template>

<script setup>
import { ref, watch } from 'vue';
import Card from './ui/Card.vue';
import Label from './ui/Label.vue';
import Input from './ui/Input.vue';
import Textarea from './ui/Textarea.vue';
import Button from './ui/Button.vue';

const props = defineProps({
  video: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['submit', 'cancel']);

const saving = ref(false);

const formData = ref({
  title: '',
  description: '',
  videoUrl: '',
  thumbnailUrl: '',
  matchDate: '',
  durationSeconds: null,
  totalShots: null,
  winners: null,
  errors: null,
  aces: null,
  doubleFaults: null,
  runningDistanceMeters: null
});

// Load video data if editing
watch(() => props.video, (newVideo) => {
  if (newVideo && props.isEdit) {
    formData.value = {
      title: newVideo.title || '',
      description: newVideo.description || '',
      videoUrl: newVideo.videoUrl || '',
      thumbnailUrl: newVideo.thumbnailUrl || '',
      matchDate: newVideo.matchDate || '',
      durationSeconds: newVideo.durationSeconds || null,
      totalShots: newVideo.totalShots || null,
      winners: newVideo.winners || null,
      errors: newVideo.errors || null,
      aces: newVideo.aces || null,
      doubleFaults: newVideo.doubleFaults || null,
      runningDistanceMeters: newVideo.runningDistanceMeters || null
    };
  }
}, { immediate: true });

const handleSubmit = async () => {
  saving.value = true;
  try {
    await emit('submit', formData.value);
    if (!props.isEdit) {
      // Reset form after creating
      formData.value = {
        title: '',
        description: '',
        videoUrl: '',
        thumbnailUrl: '',
        matchDate: '',
        durationSeconds: null,
        totalShots: null,
        winners: null,
        errors: null,
        aces: null,
        doubleFaults: null,
        runningDistanceMeters: null
      };
    }
  } finally {
    saving.value = false;
  }
};
</script>
