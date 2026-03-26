<template>
  <div class="dashboard-container">
    <header class="page-header">
      <h1>Módulo de Citas</h1>
      <p>Gestiona y visualiza la agenda de la clínica.</p>
    </header>

    <AppointmentForm @cita-creada="fetchAppointments" />

    <div class="card">
      <div class="card-header-flex">
        <h3>📋 Citas Programadas</h3>
        <span v-if="loading" class="spinner">Loading...</span>
      </div>

      <div class="table-responsive">
        <table class="modern-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Paciente</th>
            <th>Especialista</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Estado</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="cita in appointments" :key="cita.id">
            <td class="id-column">#{{ cita.id }}</td>
            <td class="fw-bold">{{ cita.pacienteNombre }}</td>
            <td>{{ cita.especialista }}</td>
            <td>{{ cita.fecha }}</td>
            <td>{{ cita.hora }}</td>
            <td>
                <span class="badge" :class="cita.estado.toLowerCase()">
                  {{ cita.estado }}
                </span>
            </td>
          </tr>
          <tr v-if="!loading && appointments.length === 0">
            <td colspan="6" class="empty-state">
              <div class="empty-icon">📭</div>
              <p>No hay citas registradas en el sistema.</p>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';
import AppointmentForm from './AppointmentForm.vue';

const appointments = ref([]);
const loading = ref(true);

const fetchAppointments = async () => {
  loading.value = true;
  try {
    const response = await api.get('/appointments/datos');
    appointments.value = response.data;
  } catch (error) {
    console.error("Error al traer las citas:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchAppointments();
});
</script>

<style scoped>
.dashboard-container { max-width: 1200px; margin: 0 auto; }

.page-header { margin-bottom: 25px; }
.page-header h1 { font-size: 26px; color: #2d3748; margin: 0 0 5px 0; }
.page-header p { color: #718096; margin: 0; font-size: 15px; }

/* Reutilizamos el estilo de tarjeta para la tabla */
.card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.03);
  padding: 25px 30px;
  border: 1px solid #f0f4f8;
}

.card-header-flex { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.card-header-flex h3 { color: #2c3e50; margin: 0; font-size: 18px; }
.spinner { font-size: 12px; color: #189a8d; font-weight: bold; background: #e0f7f4; padding: 4px 10px; border-radius: 20px; }

/* Diseño de la Tabla */
.table-responsive { overflow-x: auto; }
.modern-table { width: 100%; border-collapse: collapse; text-align: left; }

.modern-table th {
  background-color: #f8fafc;
  color: #4a5568;
  font-weight: 600;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  padding: 14px 15px;
  border-bottom: 2px solid #e2e8f0;
}

.modern-table td { padding: 16px 15px; border-bottom: 1px solid #edf2f7; color: #4a5568; font-size: 14px; }
.modern-table tr:last-child td { border-bottom: none; }
.modern-table tr:hover { background-color: #fbfdfc; }

.id-column { color: #a0aec0; font-weight: 600; font-size: 13px; }
.fw-bold { font-weight: 600; color: #2d3748; }

/* Badges para los estados (puedes agregar más clases según tus estados de Spring) */
.badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  display: inline-block;
}
.badge.pendiente { background-color: #feebc8; color: #c05621; }
.badge.completada { background-color: #c6f6d5; color: #22543d; }
.badge.cancelada { background-color: #fed7d7; color: #822727; }

/* Estado vacío */
.empty-state { text-align: center; padding: 40px 0 !important; color: #a0aec0; }
.empty-icon { font-size: 40px; margin-bottom: 10px; }
</style>