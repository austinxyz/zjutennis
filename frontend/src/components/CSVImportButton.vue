<template>
  <div>
    <input
      ref="fileInput"
      type="file"
      accept=".csv"
      class="hidden"
      @change="handleFileSelect"
    />
    <Button
      @click="$refs.fileInput.click()"
      :disabled="uploading"
      variant="outline"
    >
      <Upload class="w-4 h-4 mr-2" />
      {{ uploading ? 'Uploading...' : 'Import from CSV' }}
    </Button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Upload } from 'lucide-vue-next';
import Button from './ui/Button.vue';
import { parseSwingVisionCSV } from '../utils/swingVisionParser';
import videoAnalysisService from '../services/videoAnalysisService';

const props = defineProps({
  videoId: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['imported']);

const fileInput = ref(null);
const uploading = ref(false);

const handleFileSelect = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  try {
    uploading.value = true;

    // Parse CSV file
    const parsedData = await parseSwingVisionCSV(file);

    // Emit the parsed data to parent component
    emit('imported', parsedData);

    // Clear file input
    fileInput.value.value = '';
  } catch (error) {
    console.error('Error importing CSV:', error);
    alert('Failed to import CSV: ' + error.message);
  } finally {
    uploading.value = false;
  }
};
</script>
