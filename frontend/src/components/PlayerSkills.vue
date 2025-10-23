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

      <div class="space-y-2">
        <Label for="forehand">Forehand</Label>
        <select
          id="forehand"
          v-model.number="localSkills.forehand"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="backhand">Backhand</Label>
        <select
          id="backhand"
          v-model.number="localSkills.backhand"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="baseline">Baseline</Label>
        <select
          id="baseline"
          v-model.number="localSkills.baseline"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="volley">Volley</Label>
        <select
          id="volley"
          v-model.number="localSkills.volley"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="smash">Smash</Label>
        <select
          id="smash"
          v-model.number="localSkills.smash"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="serve">Serve</Label>
        <select
          id="serve"
          v-model.number="localSkills.serve"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="returnServe">Return Serve</Label>
        <select
          id="returnServe"
          v-model.number="localSkills.returnServe"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select rating</option>
          <option v-for="rating in technicalRatings" :key="rating" :value="rating">{{ rating }}</option>
        </select>
      </div>

      <!-- Mental & Physical Skills -->
      <div class="col-span-full mt-6">
        <h3 class="text-lg font-semibold text-foreground mb-4">Mental & Physical</h3>
      </div>

      <div class="space-y-2">
        <Label for="mental">Mental</Label>
        <select
          id="mental"
          v-model.number="localSkills.mental"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select mental level</option>
          <option :value="1">1 - Fragile</option>
          <option :value="2">2 - Reactive</option>
          <option :value="3">3 - Stable</option>
          <option :value="4">4 - Elite</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="movement">Movement</Label>
        <select
          id="movement"
          v-model.number="localSkills.movement"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select movement level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="fitness">Fitness</Label>
        <select
          id="fitness"
          v-model.number="localSkills.fitness"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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

      <div class="space-y-2">
        <Label for="courtPositioning">Court Positioning</Label>
        <select
          id="courtPositioning"
          v-model.number="localSkills.courtPositioning"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="shotSelection">Shot Selection</Label>
        <select
          id="shotSelection"
          v-model.number="localSkills.shotSelection"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <option :value="null">Select level</option>
          <option :value="1">1 - Poor</option>
          <option :value="2">2 - Basic</option>
          <option :value="3">3 - Intermediate</option>
          <option :value="4">4 - Advanced</option>
          <option :value="5">5 - Elite</option>
        </select>
      </div>

      <div class="space-y-2">
        <Label for="competitiveSpirit">Competitive Spirit</Label>
        <select
          id="competitiveSpirit"
          v-model.number="localSkills.competitiveSpirit"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
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
      <div class="space-y-2">
        <Label for="strengths">Strengths</Label>
        <Textarea
          id="strengths"
          v-model="localSkills.strengths"
          :rows="3"
          placeholder="Describe player strengths..."
        />
      </div>

      <div class="space-y-2">
        <Label for="weaknesses">Weaknesses</Label>
        <Textarea
          id="weaknesses"
          v-model="localSkills.weaknesses"
          :rows="3"
          placeholder="Describe areas for improvement..."
        />
      </div>

      <div class="space-y-2">
        <Label for="notes">Notes</Label>
        <Textarea
          id="notes"
          v-model="localSkills.notes"
          :rows="3"
          placeholder="Additional notes about the player..."
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import Label from './ui/Label.vue';
import Textarea from './ui/Textarea.vue';

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
