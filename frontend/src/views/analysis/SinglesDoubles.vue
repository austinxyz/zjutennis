<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-foreground">Singles / Doubles Analysis</h1>
      <p class="text-muted-foreground mt-2">
        Separate tracking and analysis for singles and doubles performance
      </p>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <Users2 class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Singles / Doubles Analysis</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Maintain separate statistics and analytics for singles and doubles play,
            with partnership chemistry analysis.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• Independent singles/doubles data (win rate, UTR, matches)</p>
            <p>• Doubles partnership analysis (frequency, win rate)</p>
            <p>• Partnership chemistry scoring with AI evaluation</p>
            <p>• Optimal pairing suggestions and opponent analysis</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import { Users2 } from 'lucide-vue-next';
</script>
