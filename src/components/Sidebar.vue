<template>
  <aside class="sidebar">
    <div class="brand">
      <div class="logo-icon">🏥</div>
      <div class="brand-text">
        <h2>NexusClinic</h2>
        <p>v1.0</p>
      </div>
    </div>

    <nav class="menu">
      <p class="menu-label">MÓDULOS PRINCIPALES</p>
      <ul>
        <li class="menu-item" :class="{ active: router.name === 'home' }" @click="router.push('/')">
          <span class="icon">📅</span>
          <span class="text">Citas</span>
        </li>
        <li class="menu-item">
          <span class="icon">👥</span>
          <span class="text">Pacientes</span>
        </li>
        <li class="menu-item">
          <span class="icon">🩺</span>
          <span class="text">Especialistas</span>
        </li>
        <li class="menu-item" :class="{ active: router.name === 'rrhh' }" @click="router.push('/rrhh')">
          <span class="icon">🏢</span>
          <span class="text">RRHH</span>
        </li>
      </ul>
    </nav>

    <div class="sidebar-footer">
      <button @click="cerrarSesion" class="logout-btn">
        <span class="icon">🚪</span>
        <span class="text">Cerrar Sesión</span>
      </button>
    </div>
  </aside>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

// Función para destruir el token y sacar al usuario del sistema
const cerrarSesion = () => {
  localStorage.removeItem('token');
  router.push('/login');
};
</script>

<style scoped>
/* Contenedor principal del sidebar */
.sidebar {
  width: 260px;
  height: 100vh;
  background-color: #ffffff;
  box-shadow: 4px 0 20px rgba(24, 154, 141, 0.08); /* Sombra suave con el tono verde */
  position: fixed;
  left: 0;
  top: 0;
  display: flex;
  flex-direction: column;
  z-index: 1000;
  border-right: 1px solid #f0f0f0;
}

/* Área de la marca/logo */
.brand {
  padding: 30px 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  border-bottom: 1px solid #f2fdfa;
}

.logo-icon {
  font-size: 32px;
  background: #e0f7f4;
  padding: 10px;
  border-radius: 12px;
}

.brand-text h2 {
  margin: 0;
  color: #2c3e50;
  font-size: 22px;
  font-weight: 800;
}

.brand-text p {
  margin: 0;
  color: #189a8d;
  font-size: 13px;
  font-weight: 600;
}

/* Navegación */
.menu {
  padding: 20px 15px;
  flex-grow: 1; /* Empuja el footer hacia abajo */
}

.menu-label {
  font-size: 11px;
  color: #a0aec0;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 15px;
  padding-left: 10px;
}

.menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

/* Elementos de la lista */
.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 15px;
  border-radius: 10px;
  color: #4a5568;
  font-weight: 600;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.menu-item:hover {
  background-color: #f2fdfa;
  color: #189a8d;
  transform: translateX(5px); /* Pequeño efecto de deslizamiento al pasar el mouse */
}

/* Estado Activo (La página en la que estamos) */
.active {
  background-color: #189a8d;
  color: #ffffff;
  box-shadow: 0 4px 10px rgba(24, 154, 141, 0.3);
}

.active:hover {
  background-color: #148075;
  color: #ffffff;
  transform: none; /* Quitamos el deslizamiento si ya está activo */
}

/* Footer y botón de cerrar sesión */
.sidebar-footer {
  padding: 20px;
  border-top: 1px solid #f2fdfa;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 12px;
  background-color: #fff;
  border: 1px solid #ffcbd1;
  color: #e53e3e;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background-color: #fff5f5;
  border-color: #e53e3e;
}
</style>