<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Match Planning</h1>
        <p class="text-muted-foreground mt-2">
          Plan and prepare for upcoming matches
        </p>
      </div>
      <Button>
        <Plus class="w-4 h-4 mr-2" />
        Create Plan
      </Button>
    </div>

    <Card>
      <CardContent class="pt-6">
        <div class="text-center py-12">
          <Calendar class="w-16 h-16 mx-auto text-muted-foreground mb-4" />
          <h3 class="text-lg font-semibold mb-2">Future Match Planning</h3>
          <p class="text-muted-foreground mb-6 max-w-md mx-auto">
            Plan upcoming matches with detailed rules, player availability,
            and automated lineup suggestions.
          </p>
          <div class="space-y-2 text-sm text-muted-foreground">
            <p>• Match rules and format setup (doubles count, external player rules)</p>
            <p>• Confirmed players, potential players, external players management</p>
            <p>• System auto-generates potential lineup combinations</p>
            <p>• Opponent team analysis with AI predictions</p>
            <p>• Player availability and participation tracking</p>
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
import { Calendar, Plus } from 'lucide-vue-next';
</script>
