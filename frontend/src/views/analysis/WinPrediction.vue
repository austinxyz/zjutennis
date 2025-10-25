<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-foreground">Win Prediction</h1>
      <p class="text-muted-foreground mt-2">
        AI-powered match outcome prediction for singles and doubles
      </p>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <Brain class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Win Prediction Model</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Predict match outcomes using machine learning models trained on player
            statistics and historical match data.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p><strong>Singles Prediction:</strong></p>
            <p>• Input: Player stats, recent form, head-to-head history</p>
            <p>• Output: Win probability + key influence factors</p>
            <p class="mt-4"><strong>Doubles Prediction:</strong></p>
            <p>• Input: Partnership data, opponent analysis</p>
            <p>• Output: Win probability + tactical recommendations</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import { Brain } from 'lucide-vue-next';
</script>
