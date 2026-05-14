import { createRouter, createWebHistory } from 'vue-router'
import product_catalog from "../views/product_catalog.vue";
import Login from "@/components/Login.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/products',
      name: 'products',
      component: product_catalog,
      meta: { requiresAuth: true }
    }
  ],
})

router.beforeEach((to, from, next) => {
  // Busca el token
  const token = sessionStorage.getItem('token');

  // Revisa si la ruta a la que quiere ir tiene la etiqueta 'requiresAuth'
  const requiereAutenticacion = to.matched.some(record => record.meta.requiresAuth);

  if (requiereAutenticacion && !token) {
    // No token, no pass off to login with ya
    next('/login');
  }
  else if (to.name === 'login' && token) {
    // Has a token?,  Take him to default option
    next('/products');
  }
  else {
    // Escenario 3: Todo está en orden, déjalo pasar.
    next();
  }
});

export default router