<template>
  <div class="space-y-6">
    <div class="mb-6">
      <p class="text-sm text-muted-foreground">Select rating for each technical skill (2.5 - 5.5)</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <!-- Technical Skills -->
      <div class="col-span-full">
        <h3 class="text-lg font-semibold text-foreground mb-4">Technical Skills</h3>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Forehand</label>
        <select
          v-model.number="localSkills.forehand"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Backhand</label>
        <select
          v-model.number="localSkills.backhand"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Baseline</label>
        <select
          v-model.number="localSkills.baseline"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Volley</label>
        <select
          v-model.number="localSkills.volley"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Smash</label>
        <select
          v-model.number="localSkills.smash"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Serve</label>
        <select
          v-model.number="localSkills.serve"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Return Serve</label>
        <select
          v-model.number="localSkills.returnServe"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <!-- Mental & Physical Skills -->
      <div class="col-span-full mt-6">
        <h3 class="text-lg font-semibold text-foreground mb-4">Mental & Physical</h3>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Mental</label>
        <select
          v-model.number="localSkills.mental"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select mental level</option>
          <option :value="1">1 - Fragile</option>
          <option :value="2">2 - Reactive</option>
          <option :value="3">3 - Stable</option>
          <option :value="4">4 - Elite</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Movement</label>
        <select
          v-model.number="localSkills.movement"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select movement level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Fitness</label>
        <select
          v-model.number="localSkills.fitness"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select fitness level</option>
          <option :value="1">1</option>
          <option :value="2">2</option>
          <option :value="3">3</option>
          <option :value="4">4</option>
          <option :value="5">5</option>
          <option :value="6">6</option>
          <option :value="7">7</option>
          <option :value="8">8</option>
          <option :value="9">9</option>
          <option :value="10">10</option>
        </select>
      </div>

      <!-- Strategy & Tactics -->
      <div class="col-span-full mt-6">
        <h3 class="text-lg font-semibold text-foreground mb-4">Strategy & Tactics</h3>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Court Positioning</label>
        <select
          v-model.number="localSkills.courtPositioning"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Shot Selection</label>
        <select
          v-model.number="localSkills.shotSelection"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Competitive Spirit</label>
        <select
          v-model.number="localSkills.competitiveSpirit"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option :value="null">Select level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>
    </div>

    <!-- Notes Section -->
    <div class="grid grid-cols-1 gap-6 mt-6">
      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Strengths</label>
        <textarea
          v-model="localSkills.strengths"
          rows="3"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        ></textarea>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Weaknesses</label>
        <textarea
          v-model="localSkills.weaknesses"
          rows="3"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        ></textarea>
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Notes</label>
        <textarea
          v-model="localSkills.notes"
          rows="3"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        ></textarea>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  skills: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update:skills']);

// Technical skill ratings: 2.5 to 5.5 in 0.5 increments
const technicalRatings = [2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5];

const localSkills = ref({ ...props.skills });

watch(localSkills, (newValue) => {
  emit('update:skills', newValue);
}, { deep: true });

watch(() => props.skills, (newValue) => {
  localSkills.value = { ...newValue };
}, { deep: true });
</script>
