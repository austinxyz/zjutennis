<template>
  <div class="space-y-6">
    <!-- Player Selection (only for new analysis) -->
    <div v-if="isNew" class="space-y-4">
      <h5 class="font-semibold text-md border-b pb-2">Select Player</h5>
      <div>
        <Label for="playerId">Player *</Label>
        <select
          id="playerId"
          v-model="formData.playerId"
          class="w-full px-3 py-2 border border-input rounded-md bg-background"
          required
        >
          <option value="">Select a player</option>
          <option v-for="player in availablePlayers" :key="player.id" :value="player.id">
            {{ player.name }}
          </option>
        </select>
      </div>
    </div>

    <!-- Strength Section -->
    <div class="space-y-4">
      <h5 class="font-semibold text-md border-b pb-2">Strengths</h5>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <Label for="forehand">Forehand Score (1-5)</Label>
          <Input
            id="forehand"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.strengthForehandScore"
          />
        </div>
        <div>
          <Label for="serve">Serve Score (1-5)</Label>
          <Input
            id="serve"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.strengthServeScore"
          />
        </div>
        <div>
          <Label for="volley">Volley Score (1-5)</Label>
          <Input
            id="volley"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.strengthVolleyScore"
          />
        </div>
        <div>
          <Label for="movement">Movement Score (1-5)</Label>
          <Input
            id="movement"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.strengthMovementScore"
          />
        </div>
      </div>

      <div>
        <Label for="strengthSummary">Strength Summary</Label>
        <Textarea
          id="strengthSummary"
          v-model="formData.strengthSummary"
          rows="3"
          placeholder="Describe the player's key strengths..."
        />
      </div>
    </div>

    <!-- Weakness Section -->
    <div class="space-y-4">
      <h5 class="font-semibold text-md border-b pb-2">Weaknesses</h5>

      <div class="grid grid-cols-3 gap-4">
        <div>
          <Label for="backhand">Backhand Score (1-5)</Label>
          <Input
            id="backhand"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.weaknessBackhandScore"
          />
        </div>
        <div>
          <Label for="consistency">Consistency Score (1-5)</Label>
          <Input
            id="consistency"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.weaknessConsistencyScore"
          />
        </div>
        <div>
          <Label for="pressure">Under Pressure Score (1-5)</Label>
          <Input
            id="pressure"
            type="number"
            step="0.1"
            min="1"
            max="5"
            v-model.number="formData.weaknessPressureScore"
          />
        </div>
      </div>

      <div>
        <Label for="weaknessSummary">Weakness Summary</Label>
        <Textarea
          id="weaknessSummary"
          v-model="formData.weaknessSummary"
          rows="3"
          placeholder="Describe areas for improvement..."
        />
      </div>
    </div>

    <!-- Tactical Analysis Section -->
    <div class="space-y-4">
      <h5 class="font-semibold text-md border-b pb-2">Tactical Analysis</h5>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <Label for="tacticalStyle">Tactical Style</Label>
          <select
            id="tacticalStyle"
            v-model="formData.tacticalStyle"
            class="w-full px-3 py-2 border border-input rounded-md bg-background"
          >
            <option value="">Select style</option>
            <option value="aggressive-baseliner">Aggressive Baseliner</option>
            <option value="counter-puncher">Counter Puncher</option>
            <option value="all-court-player">All Court Player</option>
            <option value="serve-and-volley">Serve and Volley</option>
          </select>
        </div>
        <div>
          <Label for="aggression">Aggression Index (0-100)</Label>
          <Input
            id="aggression"
            type="number"
            step="0.1"
            min="0"
            max="100"
            v-model.number="formData.aggressionIndex"
          />
        </div>
        <div>
          <Label for="netApproach">Net Approach Frequency (%)</Label>
          <Input
            id="netApproach"
            type="number"
            step="0.1"
            min="0"
            max="100"
            v-model.number="formData.netApproachFrequency"
          />
        </div>
        <div>
          <Label for="baseline">Baseline Rally Preference (%)</Label>
          <Input
            id="baseline"
            type="number"
            step="0.1"
            min="0"
            max="100"
            v-model.number="formData.baselineRallyPreference"
          />
        </div>
      </div>

      <div>
        <Label for="tacticalSummary">Tactical Summary</Label>
        <Textarea
          id="tacticalSummary"
          v-model="formData.tacticalSummary"
          rows="3"
          placeholder="Describe the player's tactical approach..."
        />
      </div>
    </div>

    <!-- Action Buttons -->
    <div class="flex gap-2 justify-end pt-4 border-t">
      <Button variant="outline" @click="$emit('cancel')">
        Cancel
      </Button>
      <Button @click="handleSave">
        {{ isNew ? 'Create Analysis' : 'Save Changes' }}
      </Button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import Button from './ui/Button.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import Textarea from './ui/Textarea.vue';

const props = defineProps({
  analysis: {
    type: Object,
    required: true
  },
  isNew: {
    type: Boolean,
    default: false
  },
  availablePlayers: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['save', 'cancel']);

const formData = ref({ ...props.analysis });

// Watch for changes in props.analysis
watch(() => props.analysis, (newValue) => {
  formData.value = { ...newValue };
}, { deep: true });

const handleSave = () => {
  emit('save', formData.value);
};
</script>
