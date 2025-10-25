<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-foreground">Match Summary</h1>
      <p class="text-muted-foreground mt-2">
        AI-generated match performance summaries and insights
      </p>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <FileText class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Match Performance Summary</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Automatically generate comprehensive match summaries with team strengths,
            improvement areas, and AI-powered tactical recommendations.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• System-generated overall performance summary</p>
            <p>• Team strengths and improvement areas</p>
            <p>• Key performance highlights and statistics</p>
            <p>• AI-assisted summary report writing</p>
            <p>• Opponent analysis and evaluation</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import { FileText } from 'lucide-vue-next';
</script>
