<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Match Records</h1>
        <p class="text-muted-foreground mt-2">
          Manage and analyze match history and results
        </p>
      </div>
      <Button>
        <Plus class="w-4 h-4 mr-2" />
        Add Match
      </Button>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <Trophy class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Match Records Management</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Record and track all team matches with comprehensive details including
            lineups, results, and performance analytics.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• Basic info: match name, date, location, level</p>
            <p>• Team captains, participating players, opponent information</p>
            <p>• Match structure: rounds → games → players/scores/results</p>
            <p>• Prediction vs actual comparison</p>
            <p>• Attachments: videos, photos, post-match summaries</p>
          </div>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import Button from '../../components/ui/Button.vue';
import { Trophy, Plus } from 'lucide-vue-next';
</script>
