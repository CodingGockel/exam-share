import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView, meta: { title: 'Altklausuren' } },
    {
      path: '/suchen',
      name: 'search',
      component: () => import('./views/SearchView.vue'),
      meta: { title: 'Klausuren suchen' },
    },
    {
      path: '/hochladen',
      name: 'upload',
      component: () => import('./views/UploadView.vue'),
      meta: { title: 'Klausur hochladen' },
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
  scrollBehavior: () => ({ top: 0 }),
})

router.afterEach((to) => {
  const title = typeof to.meta.title === 'string' ? to.meta.title : ''
  document.title = title ? `${title} – Prüfprot` : 'Prüfprot'
})

export default router
