<template>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
    <div class="space-y-1">
      <Label for="name">Name *</Label>
      <Input
        id="name"
        v-model="localPlayer.name"
        type="text"
        required
        placeholder="Enter player name"
      />
    </div>

    <div class="space-y-1">
      <Label for="email">Email</Label>
      <Input
        id="email"
        v-model="localPlayer.email"
        type="email"
        placeholder="player@example.com"
      />
    </div>

    <div class="space-y-1">
      <Label for="gender">Gender</Label>
      <select
        id="gender"
        v-model="localPlayer.gender"
        class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
      >
        <option value="">Select gender</option>
        <option value="male">Male</option>
        <option value="female">Female</option>
        <option value="other">Other</option>
      </select>
    </div>

    <div class="space-y-1">
      <Label for="city">City</Label>
      <Input
        id="city"
        v-model="localPlayer.city"
        type="text"
        placeholder="San Francisco"
      />
    </div>

    <div class="space-y-1">
      <Label for="country">Country</Label>
      <Input
        id="country"
        v-model="localPlayer.country"
        type="text"
        placeholder="USA"
      />
    </div>

    <div class="space-y-1">
      <Label for="phoneNumber">Phone Number</Label>
      <Input
        id="phoneNumber"
        v-model="localPlayer.phoneNumber"
        type="tel"
        placeholder="+1 234 567 8900"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';

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
