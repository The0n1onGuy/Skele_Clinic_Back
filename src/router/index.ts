import { createRouter, createWebHistory } from 'vue-router'
import AppointmentsList from '../components/AppointmentsList.vue'
import Login from '../components/Login.vue'
import DepartmentList from "../components/rrhh/DepartmentList.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/',
      name: 'home',
      component: AppointmentsList,
      // Etiquetamos esta ruta como "Privada"
      meta: { requiresAuth: true }
    },
    {
      path: '/rrhh',
      name: 'rrhh',
      component: DepartmentList, // Aquí se cargará tu tabla de departamentos
      meta: { requiresAuth: true }
    }
    // Al crear las rutas de Pacientes o RRHH, también poner meta: { requiresAuth: true }
  ],
})

router.beforeEach((to, from, next) => {
  // Busca el token
  const token = localStorage.getItem('token');

  // Revisa si la ruta a la que quiere ir tiene la etiqueta 'requiresAuth'
  const requiereAutenticacion = to.matched.some(record => record.meta.requiresAuth);

  if (requiereAutenticacion && !token) {
    // Si no tiene token se queda en login
    next('/login');
  }
  else if (to.name === 'login' && token) {
    // Quiere ir al login pero ya tiene token entonces manda a la pagina principal
    next('/');
  }
  else {
    // Escenario 3: Todo está en orden, déjalo pasar.
    next();
  }
});

export default router