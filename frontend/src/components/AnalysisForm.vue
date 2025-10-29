<template>
  <Card>
    <CardContent class="pt-6">
      <h3 class="text-lg font-semibold mb-4">{{ isEdit ? 'Edit Analysis' : 'Add Player Analysis' }}</h3>

      <form @submit.prevent="handleSubmit" class="space-y-4">
        <!-- Player Selection -->
        <div>
          <label class="block text-sm font-medium mb-2">Player *</label>
          <select
            v-model="formData.playerId"
            required
            class="w-full px-3 py-2 border rounded-md"
            :disabled="isEdit"
          >
            <option value="">Select a player</option>
            <option
              v-for="player in availablePlayers"
              :key="player.id"
              :value="player.id"
            >
              {{ player.name }}
            </option>
          </select>
        </div>

        <!-- Basic Analysis Fields (Optional) -->
        <div class="border-t pt-4">
          <h4 class="font-medium mb-3">Analysis Details (Optional)</h4>

          <!-- Strengths -->
          <div class="space-y-3 mb-4">
            <h5 class="text-sm font-medium text-muted-foreground">Strengths (0-5 scale)</h5>
            <div class="grid grid-cols-2 gap-3">
              <div>
                <label class="block text-xs mb-1">Forehand</label>
                <input
                  v-model.number="formData.strengthForehandScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
              <div>
                <label class="block text-xs mb-1">Serve</label>
                <input
                  v-model.number="formData.strengthServeScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
              <div>
                <label class="block text-xs mb-1">Volley</label>
                <input
                  v-model.number="formData.strengthVolleyScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
              <div>
                <label class="block text-xs mb-1">Movement</label>
                <input
                  v-model.number="formData.strengthMovementScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
            </div>
            <div>
              <label class="block text-xs mb-1">Strength Summary</label>
              <textarea
                v-model="formData.strengthSummary"
                rows="2"
                class="w-full px-2 py-1 border rounded text-sm"
                placeholder="Overall strength assessment..."
              ></textarea>
            </div>
          </div>

          <!-- Weaknesses -->
          <div class="space-y-3 mb-4">
            <h5 class="text-sm font-medium text-muted-foreground">Weaknesses (0-5 scale)</h5>
            <div class="grid grid-cols-3 gap-3">
              <div>
                <label class="block text-xs mb-1">Backhand</label>
                <input
                  v-model.number="formData.weaknessBackhandScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
              <div>
                <label class="block text-xs mb-1">Consistency</label>
                <input
                  v-model.number="formData.weaknessConsistencyScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
              <div>
                <label class="block text-xs mb-1">Under Pressure</label>
                <input
                  v-model.number="formData.weaknessPressureScore"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  class="w-full px-2 py-1 border rounded text-sm"
                  placeholder="0-5"
                />
              </div>
            </div>
            <div>
              <label class="block text-xs mb-1">Weakness Summary</label>
              <textarea
                v-model="formData.weaknessSummary"
                rows="2"
                class="w-full px-2 py-1 border rounded text-sm"
                placeholder="Areas for improvement..."
              ></textarea>
            </div>
          </div>

          <!-- Tactical Analysis -->
          <div class="space-y-3">
            <h5 class="text-sm font-medium text-muted-foreground">Tactical Analysis</h5>
            <div>
              <label class="block text-xs mb-1">Playing Style</label>
              <input
                v-model="formData.tacticalStyle"
                type="text"
                class="w-full px-2 py-1 border rounded text-sm"
                placeholder="e.g., Baseline grinder, Serve and volley..."
              />
            </div>
            <div>
              <label class="block text-xs mb-1">Tactical Summary</label>
              <textarea
                v-model="formData.tacticalSummary"
                rows="2"
                class="w-full px-2 py-1 border rounded text-sm"
                placeholder="Strategic approach and patterns..."
              ></textarea>
            </div>
          </div>
        </div>

        <!-- Actions -->
        <div class="flex gap-2 pt-4">
          <Button type="submit" class="flex-1">
            {{ isEdit ? 'Update' : 'Create' }} Analysis
          </Button>
          <Button type="button" variant="outline" @click="$emit('cancel')">
            Cancel
          </Button>
        </div>
      </form>
    </CardContent>
  </Card>
</template>

<script setup>
import { ref, watch } from 'vue';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import Button from './ui/Button.vue';

const props = defineProps({
  videoId: {
    type: Number,
    required: true
  },
  availablePlayers: {
    type: Array,
    required: true
  },
  analysis: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['submit', 'cancel']);

const isEdit = ref(!!props.analysis);

const formData = ref({
  playerId: props.analysis?.playerId || '',
  strengthForehandScore: props.analysis?.strengthForehandScore || null,
  strengthServeScore: props.analysis?.strengthServeScore || null,
  strengthVolleyScore: props.analysis?.strengthVolleyScore || null,
  strengthMovementScore: props.analysis?.strengthMovementScore || null,
  strengthSummary: props.analysis?.strengthSummary || '',
  weaknessBackhandScore: props.analysis?.weaknessBackhandScore || null,
  weaknessConsistencyScore: props.analysis?.weaknessConsistencyScore || null,
  weaknessPressureScore: props.analysis?.weaknessPressureScore || null,
  weaknessSummary: props.analysis?.weaknessSummary || '',
  tacticalStyle: props.analysis?.tacticalStyle || '',
  tacticalSummary: props.analysis?.tacticalSummary || '',
  status: props.analysis?.status || 'pending'
});

const handleSubmit = () => {
  emit('submit', formData.value);
};
</script>
