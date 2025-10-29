<template>
  <Card class="hover:shadow-md transition-shadow">
    <CardContent class="pt-4">
      <div class="flex justify-end items-start mb-3">
        <div class="flex gap-2">
          <Button
            size="sm"
            variant="outline"
            @click="$emit('edit', analysis)"
            title="Edit analysis"
          >
            <Edit2 class="w-4 h-4" />
          </Button>
          <Button
            size="sm"
            variant="destructive"
            @click="$emit('delete', analysis.id)"
            title="Delete analysis"
          >
            <Trash2 class="w-4 h-4" />
          </Button>
        </div>
      </div>

      <!-- Strengths Summary -->
      <div v-if="hasStrengthData" class="mb-3">
        <h4 class="text-xs font-semibold text-green-700 mb-1">Strengths</h4>
        <div class="flex gap-2 mb-1">
          <Badge v-if="analysis.strengthForehandScore" variant="secondary" size="sm">
            Forehand: {{ analysis.strengthForehandScore }}/5
          </Badge>
          <Badge v-if="analysis.strengthServeScore" variant="secondary" size="sm">
            Serve: {{ analysis.strengthServeScore }}/5
          </Badge>
          <Badge v-if="analysis.strengthVolleyScore" variant="secondary" size="sm">
            Volley: {{ analysis.strengthVolleyScore }}/5
          </Badge>
          <Badge v-if="analysis.strengthMovementScore" variant="secondary" size="sm">
            Movement: {{ analysis.strengthMovementScore }}/5
          </Badge>
        </div>
        <p v-if="analysis.strengthSummary" class="text-xs text-muted-foreground line-clamp-2">
          {{ analysis.strengthSummary }}
        </p>
      </div>

      <!-- Weaknesses Summary -->
      <div v-if="hasWeaknessData" class="mb-3">
        <h4 class="text-xs font-semibold text-red-700 mb-1">Weaknesses</h4>
        <div class="flex gap-2 mb-1">
          <Badge v-if="analysis.weaknessBackhandScore" variant="secondary" size="sm">
            Backhand: {{ analysis.weaknessBackhandScore }}/5
          </Badge>
          <Badge v-if="analysis.weaknessConsistencyScore" variant="secondary" size="sm">
            Consistency: {{ analysis.weaknessConsistencyScore }}/5
          </Badge>
          <Badge v-if="analysis.weaknessPressureScore" variant="secondary" size="sm">
            Pressure: {{ analysis.weaknessPressureScore }}/5
          </Badge>
        </div>
        <p v-if="analysis.weaknessSummary" class="text-xs text-muted-foreground line-clamp-2">
          {{ analysis.weaknessSummary }}
        </p>
      </div>

      <!-- Tactical Analysis -->
      <div v-if="hasTacticalData" class="mb-3">
        <h4 class="text-xs font-semibold text-blue-700 mb-1">Tactical Style</h4>
        <p v-if="analysis.tacticalStyle" class="text-xs mb-1">
          <strong>Style:</strong> {{ analysis.tacticalStyle }}
        </p>
        <p v-if="analysis.tacticalSummary" class="text-xs text-muted-foreground line-clamp-2">
          {{ analysis.tacticalSummary }}
        </p>
      </div>

      <!-- Timestamps -->
      <div v-if="analysis.createdAt" class="flex items-center gap-3 text-xs text-muted-foreground pt-2 border-t">
        <div>
          Created: {{ formatDate(analysis.createdAt) }}
        </div>
      </div>

      <!-- Empty State -->
      <div v-if="!hasAnyData" class="text-center py-3 text-sm text-muted-foreground">
        No analysis data added yet. Click edit to add details.
      </div>
    </CardContent>
  </Card>
</template>

<script setup>
import { computed } from 'vue';
import Card from './ui/Card.vue';
import CardContent from './ui/CardContent.vue';
import Badge from './ui/Badge.vue';
import Button from './ui/Button.vue';
import { Edit2, Trash2 } from 'lucide-vue-next';

const props = defineProps({
  analysis: {
    type: Object,
    required: true
  }
});

defineEmits(['edit', 'delete']);

const hasStrengthData = computed(() => {
  return props.analysis.strengthForehandScore ||
    props.analysis.strengthServeScore ||
    props.analysis.strengthVolleyScore ||
    props.analysis.strengthMovementScore ||
    props.analysis.strengthSummary;
});

const hasWeaknessData = computed(() => {
  return props.analysis.weaknessBackhandScore ||
    props.analysis.weaknessConsistencyScore ||
    props.analysis.weaknessPressureScore ||
    props.analysis.weaknessSummary;
});

const hasTacticalData = computed(() => {
  return props.analysis.tacticalStyle || props.analysis.tacticalSummary;
});

const hasAnyData = computed(() => {
  return hasStrengthData.value ||
    hasWeaknessData.value ||
    hasTacticalData.value;
});

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};
</script>
