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
        path: 'home',
        name: 'Home',
        component: () => import("@/views/Home")
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import("@/views/Dashboard")
      },
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
        path: 'test',
        name: 'Test',
        component: () => import("@/views/Test")
      }
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
