<template>
  <div class="w-full h-full">
    <Radar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { Radar } from 'vue-chartjs';
import {
  Chart as ChartJS,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
} from 'chart.js';

// Register Chart.js components
ChartJS.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
);

const props = defineProps({
  skills: {
    type: Object,
    default: () => ({})
  }
});

const chartData = computed(() => {
  const skills = props.skills || {};

  return {
    labels: [
      'Forehand',
      'Backhand',
      'Serve',
      'Baseline',
      'Volley',
      'Smash',
      'Return Serve',
      'Mental',
      'Movement',
      'Fitness'
    ],
    datasets: [
      {
        label: 'Skill Level',
        data: [
          skills.forehand || 0,
          skills.backhand || 0,
          skills.serve || 0,
          skills.baseline || 0,
          skills.volley || 0,
          skills.smash || 0,
          skills.returnServe || 0,
          skills.mental || 0,
          skills.movement || 0,
          skills.fitness || 0
        ],
        backgroundColor: 'rgba(59, 130, 246, 0.2)',
        borderColor: 'rgb(59, 130, 246)',
        borderWidth: 2,
        pointBackgroundColor: 'rgb(59, 130, 246)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgb(59, 130, 246)'
      }
    ]
  };
});

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: true,
  scales: {
    r: {
      beginAtZero: true,
      min: 0,
      max: 5.5,
      ticks: {
        stepSize: 1,
        color: 'rgb(107, 114, 128)'
      },
      grid: {
        color: 'rgba(107, 114, 128, 0.1)'
      },
      angleLines: {
        color: 'rgba(107, 114, 128, 0.1)'
      },
      pointLabels: {
        color: 'rgb(55, 65, 81)',
        font: {
          size: 11
        }
      }
    }
  },
  plugins: {
    legend: {
      display: false
    },
    tooltip: {
      callbacks: {
        label: function(context) {
          return context.label + ': ' + context.parsed.r + '/5.5';
        }
      }
    }
  }
}));
</script>
