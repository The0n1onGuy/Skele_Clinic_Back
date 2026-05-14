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

<style src="../styles/product_catalog.css" scoped> </style>