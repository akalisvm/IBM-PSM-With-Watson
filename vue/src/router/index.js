import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import { getCookie } from "@/utils/cookie";

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: "/login",
    children: [
      {
        path: 'patients',
        name: 'Patients',
        component: () => import("@/views/Patients")
      },
      {
        path: 'questionnaires',
        name: 'Questionnaires',
        component: () => import("@/views/Questionnaires")
      },
      {
        path: 'records',
        name: 'Records',
        component: () => import("@/views/Records")
      },
      {
        path: 'events',
        name: 'Events',
        component: () => import("@/views/Events")
      },
      {
        path: 'center',
        name: 'Center',
        component: () => import("@/views/Center")
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if(to.name !== "Login" && !getCookie("user")) {
    next( {name: "Login"} )
  } else { next() }
})

export default router
