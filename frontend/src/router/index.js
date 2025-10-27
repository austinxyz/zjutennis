import { createRouter, createWebHistory } from 'vue-router';
import MainLayout from '../components/MainLayout.vue';
import Dashboard from '../views/Dashboard.vue';
import PlayerList from '../views/PlayerList.vue';
import PlayerEdit from '../views/PlayerEdit.vue';
import PlayerVideoAnalysis from '../views/PlayerVideoAnalysis.vue';

// Player Analysis Views
import VideoAnalysis from '../views/analysis/VideoAnalysis.vue';
import PlayerEvaluation from '../views/analysis/PlayerEvaluation.vue';
import GrowthTrajectory from '../views/analysis/GrowthTrajectory.vue';
import SinglesDoubles from '../views/analysis/SinglesDoubles.vue';
import WinPrediction from '../views/analysis/WinPrediction.vue';

// Match Analysis Views
import MatchList from '../views/matches/MatchList.vue';
import MatchSummary from '../views/matches/MatchSummary.vue';
import MatchVideoManagement from '../views/matches/MatchVideoManagement.vue';

// Team Composition Views
import MatchPlanning from '../views/team/MatchPlanning.vue';
import LineupOptimization from '../views/team/LineupOptimization.vue';

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: {
          title: 'Dashboard',
          description: 'Overview of your tennis analysis system'
        }
      },
      // Player Management
      {
        path: 'players',
        name: 'PlayerList',
        component: PlayerList,
        meta: {
          title: 'Player Management',
          description: 'Manage player profiles and information'
        }
      },
      {
        path: 'players/new',
        name: 'PlayerNew',
        component: PlayerEdit,
        meta: {
          title: 'Add New Player',
          description: 'Create a new player profile'
        }
      },
      {
        path: 'players/:id/edit',
        name: 'PlayerEdit',
        component: PlayerEdit,
        meta: {
          title: 'Edit Player',
          description: 'Update player information'
        }
      },
      {
        path: 'players/:playerId/videos',
        name: 'PlayerVideoAnalysis',
        component: PlayerVideoAnalysis,
        meta: {
          title: 'Player Video Analysis',
          description: 'Manage and analyze player match videos'
        }
      },
      // Player Analysis
      {
        path: 'analysis/video',
        name: 'VideoAnalysis',
        component: VideoAnalysis,
        meta: {
          title: 'Video Analysis',
          description: 'AI-powered match video analysis'
        }
      },
      {
        path: 'analysis/evaluation',
        name: 'PlayerEvaluation',
        component: PlayerEvaluation,
        meta: {
          title: 'Player Evaluation',
          description: 'Peer evaluation and feedback system'
        }
      },
      {
        path: 'analysis/growth',
        name: 'GrowthTrajectory',
        component: GrowthTrajectory,
        meta: {
          title: 'Growth Trajectory',
          description: 'Track player development over time'
        }
      },
      {
        path: 'analysis/singles-doubles',
        name: 'SinglesDoubles',
        component: SinglesDoubles,
        meta: {
          title: 'Singles / Doubles Analysis',
          description: 'Separate tracking for singles and doubles performance'
        }
      },
      {
        path: 'analysis/prediction',
        name: 'WinPrediction',
        component: WinPrediction,
        meta: {
          title: 'Win Prediction',
          description: 'AI-powered match outcome prediction'
        }
      },
      // Match Analysis
      {
        path: 'matches',
        name: 'MatchList',
        component: MatchList,
        meta: {
          title: 'Match Records',
          description: 'View and manage match history'
        }
      },
      {
        path: 'matches/:matchId/videos',
        name: 'MatchVideoManagement',
        component: MatchVideoManagement,
        meta: {
          title: 'Match Videos',
          description: 'Manage videos for this match'
        }
      },
      {
        path: 'matches/summary',
        name: 'MatchSummary',
        component: MatchSummary,
        meta: {
          title: 'Match Summary',
          description: 'AI-generated match performance summaries'
        }
      },
      // Team Composition
      {
        path: 'team/planning',
        name: 'MatchPlanning',
        component: MatchPlanning,
        meta: {
          title: 'Match Planning',
          description: 'Plan and prepare for upcoming matches'
        }
      },
      {
        path: 'team/lineup',
        name: 'LineupOptimization',
        component: LineupOptimization,
        meta: {
          title: 'Lineup Optimization',
          description: 'AI-powered team composition and strategy'
        }
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
