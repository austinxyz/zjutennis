import { createRouter, createWebHistory } from 'vue-router';
import PlayerList from '../views/PlayerList.vue';
import PlayerEdit from '../views/PlayerEdit.vue';

const routes = [
  {
    path: '/',
    redirect: '/players'
  },
  {
    path: '/players',
    name: 'PlayerList',
    component: PlayerList
  },
  {
    path: '/players/new',
    name: 'PlayerNew',
    component: PlayerEdit
  },
  {
    path: '/players/:id/edit',
    name: 'PlayerEdit',
    component: PlayerEdit
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
