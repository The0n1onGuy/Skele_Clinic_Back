<template>
  <div class="dashboard-container">
    <header class="page-header">
      <h1>Gestion de Departamentos</h1>
      <p>Consulte los departamentos</p>
    </header>

    <div class="card">
      <div class="card-header-flex">
        <h3>Departamentos</h3>
        <span v-if="loading" class="spinner">Loading...</span>
        <div class="filters">
          <select v-model="filterStatus" class="filter-select">
            <option value="ALL">Todos los estados</option>
            <option value="ACTIVE">Solo Activos</option>
            <option value="INACTIVE">Solo Inactivos</option>
          </select>
          <span v-if="loading" class="spinner">Cargando...</span>
        </div>
      </div>

      <div class="table-responsive">
        <table class="modern-table">
          <thead>
          <tr>
            <th>#</th> <th>Nombre del Departamento</th>
            <th>Estado</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(dept, index) in filteredDepartments" :key="index">
            <td class="id-column">{{ index + 1 }}</td>
            <td class="fw-bold">{{ dept.name }}</td>
            <td>
                <span class="badge" :class="dept.statusName.toLowerCase()">
                  {{ dept.statusName }}
                </span>
            </td>
          </tr>
          <tr v-if="!loading && filteredDepartments.length === 0">
            <td colspan="3" class="empty-state">
              <div class="empty-icon">🏢</div>
              <p>No se encontraron departamentos con el filtro seleccionado.</p>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '../../services/api.js';

const departments = ref([]);
const loading = ref(true);
const filterStatus = ref('ACTIVE');

const fetchDepartments = async () => {
  loading.value = true;
  try {
    const url = api.buildUrl('/rrhh/departments/all', 'rrhh');
    //const response = await api.get('/rrhh/departments/all');
    /*const response = await api.get('', {
      params: { action: 'rrhh' }
    });
    */
    const response = await api.get(url, {
      params: { action: 'rrhh' } // PHP lo usa, Spring lo ignora
    });
    departments.value = response.data.data || response.data;
  } catch (error) {
    console.error("Error al obtener departamentos:", error);
  } finally {
    loading.value = false;
  }
};

// Lógica de filtrado en el Front-end
const filteredDepartments = computed(() => {
  if (filterStatus.value === 'ALL') return departments.value;
  return departments.value.filter(d => d.statusName.toUpperCase() === filterStatus.value);
});

onMounted(fetchDepartments);
</script>

<style scoped>
/* Hereda tus estilos anteriores y añade estos para el filtro */
.filters { display: flex; align-items: center; gap: 15px; }
.filter-select {
  padding: 8px 12px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  outline: none;
  color: #4a5568;
  font-weight: 500;
}

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

/* Badges para los estados */
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

/* Colores para estados de departamento */
.badge.activo { background-color: #c6f6d5; color: #22543d; }
.badge.inactivo { background-color: #fed7d7; color: #822727; }


/* Estado vacío */
.empty-state { text-align: center; padding: 40px 0 !important; color: #a0aec0; }
.empty-icon { font-size: 40px; margin-bottom: 10px; }
</style>