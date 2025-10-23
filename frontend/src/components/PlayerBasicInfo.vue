<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Name *</label>
        <input
          v-model="localPlayer.name"
          type="text"
          required
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Email</label>
        <input
          v-model="localPlayer.email"
          type="email"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Graduation Year</label>
        <input
          v-model.number="localPlayer.graduationYear"
          type="number"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Major</label>
        <input
          v-model="localPlayer.major"
          type="text"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">City</label>
        <input
          v-model="localPlayer.city"
          type="text"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Country</label>
        <input
          v-model="localPlayer.country"
          type="text"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Phone Number</label>
        <input
          v-model="localPlayer.phoneNumber"
          type="tel"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-foreground mb-2">Gender</label>
        <select
          v-model="localPlayer.gender"
          class="w-full px-3 py-2 border border-input rounded-md focus:outline-none focus:ring-2 focus:ring-ring bg-background text-foreground"
        >
          <option value="">Select gender</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
          <option value="other">Other</option>
        </select>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  player: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update:player']);

const localPlayer = ref({ ...props.player });

watch(localPlayer, (newValue) => {
  emit('update:player', newValue);
}, { deep: true });

watch(() => props.player, (newValue) => {
  localPlayer.value = { ...newValue };
}, { deep: true });
</script>
