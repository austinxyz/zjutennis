<template>
  <div class="space-y-6">
    <!-- Player's Graduation Universities -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Graduation Universities</h3>
      <div class="space-y-6">
        <!-- University 1 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <Label for="graduationUniversity1">Graduation University 1</Label>
            <Input
              id="graduationUniversity1"
              v-model="localAlumni.graduationUniversity1"
              type="text"
              placeholder="University name"
            />
          </div>
          <div class="space-y-2">
            <Label for="graduationYear1">Graduation Year 1</Label>
            <Input
              id="graduationYear1"
              v-model.number="localAlumni.graduationYear1"
              type="number"
              placeholder="YYYY"
            />
          </div>
        </div>

        <!-- University 2 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <Label for="graduationUniversity2">Graduation University 2</Label>
            <Input
              id="graduationUniversity2"
              v-model="localAlumni.graduationUniversity2"
              type="text"
              placeholder="University name"
            />
          </div>
          <div class="space-y-2">
            <Label for="graduationYear2">Graduation Year 2</Label>
            <Input
              id="graduationYear2"
              v-model.number="localAlumni.graduationYear2"
              type="number"
              placeholder="YYYY"
            />
          </div>
        </div>

        <!-- University 3 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <Label for="graduationUniversity3">Graduation University 3</Label>
            <Input
              id="graduationUniversity3"
              v-model="localAlumni.graduationUniversity3"
              type="text"
              placeholder="University name"
            />
          </div>
          <div class="space-y-2">
            <Label for="graduationYear3">Graduation Year 3</Label>
            <Input
              id="graduationYear3"
              v-model.number="localAlumni.graduationYear3"
              type="number"
              placeholder="YYYY"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Couple's Graduation Universities -->
    <div>
      <h3 class="text-lg font-semibold text-foreground mb-4">Couple's Graduation Universities</h3>
      <div class="space-y-6">
        <!-- Couple University 1 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <Label for="coupleGraduationUniversity1">Couple's Graduation University 1</Label>
            <Input
              id="coupleGraduationUniversity1"
              v-model="localAlumni.coupleGraduationUniversity1"
              type="text"
              placeholder="University name"
            />
          </div>
          <div class="space-y-2">
            <Label for="coupleGraduationYear1">Couple's Graduation Year 1</Label>
            <Input
              id="coupleGraduationYear1"
              v-model.number="localAlumni.coupleGraduationYear1"
              type="number"
              placeholder="YYYY"
            />
          </div>
        </div>

        <!-- Couple University 2 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <Label for="coupleGraduationUniversity2">Couple's Graduation University 2</Label>
            <Input
              id="coupleGraduationUniversity2"
              v-model="localAlumni.coupleGraduationUniversity2"
              type="text"
              placeholder="University name"
            />
          </div>
          <div class="space-y-2">
            <Label for="coupleGraduationYear2">Couple's Graduation Year 2</Label>
            <Input
              id="coupleGraduationYear2"
              v-model.number="localAlumni.coupleGraduationYear2"
              type="number"
              placeholder="YYYY"
            />
          </div>
        </div>

        <!-- Couple University 3 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <Label for="coupleGraduationUniversity3">Couple's Graduation University 3</Label>
            <Input
              id="coupleGraduationUniversity3"
              v-model="localAlumni.coupleGraduationUniversity3"
              type="text"
              placeholder="University name"
            />
          </div>
          <div class="space-y-2">
            <Label for="coupleGraduationYear3">Couple's Graduation Year 3</Label>
            <Input
              id="coupleGraduationYear3"
              v-model.number="localAlumni.coupleGraduationYear3"
              type="number"
              placeholder="YYYY"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import Label from './ui/Label.vue';
import Input from './ui/Input.vue';

const props = defineProps({
  alumni: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update:alumni']);

const localAlumni = ref({ ...props.alumni });

watch(localAlumni, (newValue) => {
  emit('update:alumni', newValue);
}, { deep: true });

watch(() => props.alumni, (newValue) => {
  localAlumni.value = { ...newValue };
}, { deep: true });
</script>
