import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/user'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('@/views/user/Layout.vue'),
    children: [
      {
        path: '',
        redirect: '/user/home'
      },
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('@/views/user/Home.vue')
      },
      {
        path: 'nurses',
        name: 'UserNurses',
        component: () => import('@/views/user/Nurses.vue')
      },
      {
        path: 'forum',
        name: 'UserForum',
        component: () => import('@/views/user/Forum.vue')
      },
      {
        path: 'orders',
        name: 'UserOrders',
        component: () => import('@/views/user/Orders.vue')
      }
    ]
  },
  {
    path: '/nurse',
    name: 'Nurse',
    component: () => import('@/views/nurse/Layout.vue'),
    children: [
      {
        path: '',
        redirect: '/nurse/orders'
      },
      {
        path: 'orders',
        name: 'NurseOrders',
        component: () => import('@/views/nurse/Orders.vue')
      },
      {
        path: 'evaluations',
        name: 'NurseEvaluations',
        component: () => import('@/views/nurse/Evaluations.vue')
      }
    ]
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/Layout.vue'),
    children: [
      {
        path: '',
        redirect: '/admin/users'
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue')
      },
      {
        path: 'nurses',
        name: 'AdminNurses',
        component: () => import('@/views/admin/Nurses.vue')
      },
      {
        path: 'orders',
        name: 'AdminOrders',
        component: () => import('@/views/admin/Orders.vue')
      },
      {
        path: 'complaints',
        name: 'AdminComplaints',
        component: () => import('@/views/admin/Complaints.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
