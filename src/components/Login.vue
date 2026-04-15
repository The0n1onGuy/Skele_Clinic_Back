<template>
  <div class="login-wrapper">
    <div class="login-card">
      <div class="login-header">
        <h2>Bienvenido</h2>
        <p>Sistema de Expediente Clínico</p>
      </div>

      <form @submit.prevent="hacerLogin" class="login-form">
        <div class="form-group">
          <label>Usuario</label>
          <input
              v-model="credenciales.userName"
              type="text"
              placeholder="Ingresa tu usuario"
              required
          >
        </div>

        <div class="form-group">
          <label>Contraseña</label>
          <input
              v-model="credenciales.userPassword"
              type="password"
              placeholder="••••••••"
              required
          >
        </div>

        <button type="submit" class="btn-login">Iniciar Sesión</button>

        <div v-if="errorMsg" class="error-message">
          {{ errorMsg }}
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '../services/api';

const router = useRouter();
const errorMsg = ref('');

const credenciales = ref({
  userName: '',
  userPassword: ''
});

const hacerLogin = async () => {
  errorMsg.value = ''; // Limpiamos errores previos
  try {
    const url = api.buildUrl('/auth/login', 'auth');
    //const respuesta = await api.post('/auth/login', credenciales.value);
    /* const respuesta = await api.post('', credenciales.value, {
      params: { action: 'auth' }
    });
    */
    const respuesta = await api.post(url, credenciales.value, {
      params: { action: 'auth' } // PHP lo usa, Spring lo ignora
    });

    const token = respuesta.data.token || respuesta.data.data.token;
    localStorage.setItem('token', token);
    router.push('/');
  } catch (error) {
    console.error("Error en login:", error);
    errorMsg.value = "Credenciales incorrectas o error de servidor";
  }
};
</script>

<style scoped>
/* Contenedor que centra la tarjeta en la pantalla */
.login-wrapper {
  width: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f2fdfa; /* Color 2 de tu paleta (Fondo claro) */
}

/* La tarjeta blanca flotante */
.login-card {
  background: white;
  width: 100%;
  max-width: 420px;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(24, 154, 141, 0.15); /* Sombra teñida del color principal */
  border-top: 5px solid #189a8d; /* Acento superior (Color 1 de tu paleta) */
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #189a8d; /* Color 1 de tu paleta */
  font-size: 28px;
  margin: 0 0 5px 0;
}

.login-header p {
  color: #7f8c8d;
  margin: 0;
  font-size: 15px;
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
  font-size: 14px;
}

/* Estilo de los inputs */
.form-group input {
  padding: 12px 15px;
  border: 1px solid #b3e8e0; /* Color 3 de tu paleta */
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.3s ease;
  background-color: #fbfdfc;
}

/* Efecto al hacer clic en el input */
.form-group input:focus {
  outline: none;
  border-color: #189a8d; /* Color 1 */
  /* Un pequeño resplandor usando el Color 4 de tu paleta */
  box-shadow: 0 0 0 3px rgba(139, 226, 213, 0.4);
}

/* Estilo del botón */
.btn-login {
  width: 100%;
  padding: 14px;
  background-color: #189a8d; /* Color 1 */
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.1s ease;
  margin-top: 10px;
}

.btn-login:hover {
  background-color: #148075; /* Un tono un poco más oscuro para el hover */
}

.btn-login:active {
  transform: scale(0.98); /* Efecto de hundirse al dar clic */
}

/* Mensaje de error bonito */
.error-message {
  color: #c0392b;
  background-color: #fadbd8;
  padding: 12px;
  border-radius: 6px;
  text-align: center;
  font-size: 14px;
  margin-top: 20px;
  border: 1px solid #f5b7b1;
}
</style>