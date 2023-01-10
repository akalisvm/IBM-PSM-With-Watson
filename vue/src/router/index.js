import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import { getCookie } from "@/utils/cookie.utils";

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: "/login",
    children: [
      {
        path: 'patient',
        name: 'Patient',
        component: () => import("@/views/Patient")
      },
      {
        path: 'questionnaire',
        name: 'Questionnaire',
        component: () => import("@/views/Questionnaire")
      },
      {
        path: 'record',
        name: 'Record',
        component: () => import("@/views/Record")
      },
      {
        path: 'event',
        name: 'Event',
        component: () => import("@/views/Event")
      },
      {
        path: 'personal',
        name: 'Personal',
        component: () => import("@/views/Personal")
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
