<template>
  <div class="space-y-6">
    <div>
      <h1 class="text-2xl font-bold text-foreground">Video Analysis</h1>
      <p class="text-muted-foreground mt-2">
        Upload and analyze match videos with AI-powered insights
      </p>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <Video class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Video Analysis Module</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            This feature will allow you to upload match videos, analyze player performance,
            identify strengths and weaknesses, and receive AI-generated recommendations.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• Upload match videos or import external links</p>
            <p>• Auto-generate match summaries (shots, errors, winners)</p>
            <p>• AI analysis of strengths and improvement areas</p>
            <p>• Tactical tendency analysis (defensive/offensive)</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import { Video } from 'lucide-vue-next';
</script>
