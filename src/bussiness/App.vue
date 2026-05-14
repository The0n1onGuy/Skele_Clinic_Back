<template>
  <div id="app">
    <Sidebar v-if="route.name !== 'login'" />

    <main :class="route.name === 'login' ? 'content-full' : 'content-with-sidebar'">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import Sidebar from './components/Sidebar.vue';
// Importamos useRoute para saber en qué URL estamos
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<style>
body {
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f2fdfa;
}

#app {
  display: flex;
  min-height: 100vh;
}

/* Cuando estamos dentro del sistema (con menú) */
.content-with-sidebar {
  margin-left: 250px;
  padding: 30px;
  width: 100%;
}

/* Cuando estamos en el login (pantalla completa) */
.content-full {
  margin-left: 0;
  width: 100%;
}
</style>