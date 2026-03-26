<template>
  <div class="card">
    <div class="card-header">
      <h3>📅 Agendar Nueva Cita</h3>
      <p>Ingresa los datos para programar una consulta médica.</p>
    </div>

    <form @submit.prevent="guardarCita" class="modern-form">
      <div class="form-grid">
        <div class="form-group">
          <label>Nombre del Paciente</label>
          <input v-model="nuevaCita.pacienteNombre" type="text" placeholder="Ej. Juan Pérez" required>
        </div>

        <div class="form-group">
          <label>Médico Especialista</label>
          <input v-model="nuevaCita.especialista" type="text" placeholder="Ej. Dra. Gómez" required>
        </div>

        <div class="form-group">
          <label>Fecha</label>
          <input v-model="nuevaCita.fecha" type="date" required>
        </div>

        <div class="form-group">
          <label>Hora</label>
          <input v-model="nuevaCita.hora" type="time" required>
        </div>

        <div class="form-group full-width">
          <label>Motivo de la consulta</label>
          <textarea v-model="nuevaCita.motivo" rows="2" placeholder="Describe brevemente el síntoma o razón de la visita..."></textarea>
        </div>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-primary" :disabled="guardando">
          {{ guardando ? 'Guardando cita...' : 'Guardar Cita' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from '../services/api';

const emit = defineEmits(['cita-creada']);
const guardando = ref(false);

const nuevaCita = ref({
  pacienteNombre: '', especialista: '', fecha: '', hora: '', motivo: '', estado: 'Pendiente'
});

const guardarCita = async () => {
  guardando.value = true;
  try {
    await api.post('/appointments/create', nuevaCita.value);
    nuevaCita.value = { pacienteNombre: '', especialista: '', fecha: '', hora: '', motivo: '', estado: 'Pendiente' };
    emit('cita-creada');
  } catch (error) {
    console.error("Error:", error);
    alert("Hubo un error al guardar la cita.");
  } finally {
    guardando.value = false;
  }
};
//Todo evitar hardcode
</script>

<style scoped>
/* Estilo de Tarjeta Moderna */
.card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.03);
  padding: 25px 30px;
  margin-bottom: 30px;
  border: 1px solid #f0f4f8;
}

.card-header { margin-bottom: 20px; border-bottom: 1px solid #f0f4f8; padding-bottom: 15px; }
.card-header h3 { color: #2c3e50; margin: 0 0 5px 0; font-size: 20px; }
.card-header p { color: #7f8c8d; margin: 0; font-size: 14px; }

/* Cuadrícula del formulario */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr; /* Dos columnas del mismo tamaño */
  gap: 20px;
}

.full-width { grid-column: span 2; } /* Hace que el textarea ocupe las 2 columnas */

.form-group { display: flex; flex-direction: column; }
.form-group label { font-size: 13px; font-weight: 600; color: #4a5568; margin-bottom: 8px; }

/* Inputs con el estilo del login */
input, textarea {
  padding: 10px 14px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  background-color: #fbfdfc;
  transition: all 0.2s ease;
  font-family: inherit;
}
input:focus, textarea:focus {
  outline: none;
  border-color: #189a8d;
  box-shadow: 0 0 0 3px rgba(24, 154, 141, 0.15);
}

.form-actions { margin-top: 25px; display: flex; justify-content: flex-end; }

.btn-primary {
  background-color: #189a8d;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}
.btn-primary:hover { background-color: #148075; transform: translateY(-1px); }
.btn-primary:disabled { background-color: #a0aec0; cursor: not-allowed; transform: none; }
</style>