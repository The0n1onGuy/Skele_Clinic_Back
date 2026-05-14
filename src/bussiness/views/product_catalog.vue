<template>
  <div class="catalog-container">
    <header class="page-header">
      <h1>Catálogo de Productos</h1>
      <p>Seleccione los artículos para el carrito de ventas</p>
    </header>

    <div class="product-grid">
      <div
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="openSelection(product)"
      >
        <div class="product-image-placeholder">🖼️</div>

        <div class="product-info">
          <h3 class="product-name">{{ truncate(product.name, 30) }}</h3>

          <div class="price-container">
            <span v-if="product.discountPrice" class="original-price strike">
              ${{ product.base_price }}
            </span>
            <span class="current-price">
              ${{ product.discountPrice || product.base_price }}
            </span>
            <span v-if="product.discountPrice" class="discount-badge">
              -{{ calculatePercent(product) }}%
            </span>
          </div>
        </div>

        <div class="product-details-hover">
          <p class="full-name"><strong>{{ product.name }}</strong></p>
          <p class="category">Categoría: {{ product.category }}</p>
          <div class="details-prices">
            <p>Precio Base: ${{ product.base_price }}</p>
            <p v-if="product.discountPrice" class="text-success">
              Oferta: ${{ product.discountPrice }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="selectedProduct" class="modal-overlay">
      <div class="selection-card">
        <h3>Confirmar Selección</h3>
        <p class="product-confirm-name">{{ selectedProduct.name }}</p>

        <div class="quantity-picker">
          <label>Cantidad:</label>
          <input type="number" v-model.number="quantity" min="1" class="qty-input">
        </div>

        <div class="calculation-summary">
          <p v-if="selectedProduct.discountPrice" class="discount-alert">
            ¡Este producto tiene un descuento activo!
          </p>
          <p>Subtotal producto: <strong>${{ currentSelectionTotal }}</strong></p>
          <hr>
          <p>Valor actual en carrito: ${{ currentCartValue }}</p>
          <p class="final-sum">Total estimado: <strong>${{ currentCartValue + currentSelectionTotal }}</strong></p>
        </div>

        <p class="confirm-question">¿Esta selección de producto es correcta?</p>

        <div class="modal-actions">
          <button @click="confirmAdd" class="btn-yes">SÍ</button>
          <button @click="selectedProduct = null" class="btn-cancel">Cancelar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '../services/api.js';

const products = ref([]);
const selectedProduct = ref(null);
const quantity = ref(1);
const currentCartValue = ref(0); // Esto debería venir de tu backend o store

const fetchProducts = async () => {
  try {
    /*
    const response = await api.get(api.route('pos.products'));
    products.value = response.data.data;
     */
    const url = api.buildUrl('/pos/cart/products', 'products');
    const response = await api.get(url, {
      params: { action: 'products' } // used in PHP
    });
    products.value = response.data.data || response.data;
  } catch (error) {
    console.error("Error cargando productos:", error);
  }
};

const truncate = (text, limit) => text.length > limit ? text.substring(0, limit) + '...' : text;

const calculatePercent = (p) => Math.round(((p.price - p.discountPrice) / p.price) * 100);

const currentSelectionTotal = computed(() => {
  if (!selectedProduct.value) return 0;
  const price = selectedProduct.value.discountPrice || selectedProduct.value.price;
  return price * quantity.value;
});

const openSelection = (product) => {
  selectedProduct.value = product;
  quantity.value = 1;
};

const confirmAdd = async () => {
  // Aquí iría tu lógica para enviar al carrito en Spring Boot
  console.log("Agregando al carrito...", selectedProduct.value.id, quantity.value);
  selectedProduct.value = null;
};

onMounted(fetchProducts);
</script>

<style scoped>
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}

.product-card {
  background: #1b2838; /* Color oscuro estilo Steam */
  color: white;
  border-radius: 4px;
  position: relative;
  cursor: pointer;
  transition: transform 0.2s;
  overflow: hidden;
}

.product-card:hover {
  transform: scale(1.05);
  z-index: 10;
}

.product-image-placeholder {
  height: 120px;
  background: #000;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 40px;
}

.product-info { padding: 10px; }
.product-name { font-size: 14px; margin: 0 0 10px 0; height: 35px; }

.price-container { display: flex; align-items: center; gap: 8px; }
.strike { text-decoration: line-through; color: #888; font-size: 12px; }
.current-price { color: #a3d200; font-weight: bold; } /* Verde Steam */

.discount-badge {
  background: #4c6b22;
  color: #beee11;
  padding: 2px 4px;
  font-size: 12px;
  font-weight: bold;
}

/* Hover Details hidden by default */
.product-details-hover {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(27, 40, 56, 0.95);
  padding: 15px;
  display: none;
  flex-direction: column;
  justify-content: center;
}

.product-card:hover .product-details-hover { display: flex; }

/* Modal Styles */
.modal-overlay {
  position: fixed; top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0,0,0,0.7);
  display: flex; justify-content: center; align-items: center;
  z-index: 2000;
}

.selection-card {
  background: white; padding: 30px; border-radius: 12px;
  width: 400px; color: #2c3e50;
}

.btn-yes { background: #189a8d; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; }
.btn-cancel { background: #e2e8f0; border: none; padding: 10px 20px; border-radius: 5px; margin-left: 10px; }
</style>