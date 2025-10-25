<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-foreground">Player Evaluation</h1>
      <p class="text-muted-foreground mt-2">
        Peer evaluation system for comprehensive player assessment
      </p>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <MessageSquare class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Player Evaluation System</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Collect anonymous evaluations from teammates and opponents to build
            comprehensive player profiles.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• Anonymous peer evaluations (technical, mental, teamwork)</p>
            <p>• Rating dimensions: consistency, tactical understanding, cooperation</p>
            <p>• Overall impression trend charts over time</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import { MessageSquare } from 'lucide-vue-next';
</script>
