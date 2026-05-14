<template>
  <aside class="sidebar">
    <div class="brand">
      <div class="logo-icon">🏥</div>
      <div class="brand-text">
        <h2>Store name</h2>
        <p>ver 1.0</p>

        <div class="language-switcher">
          <button @click="changeLanguage('es')" :class="{ active: locale === 'es' }">ES</button>
          <button @click="changeLanguage('en')" :class="{ active: locale === 'en' }">EN</button>
        </div>

      </div>
    </div>

    <nav class="menu">
      <p class="menu-label">{{$t('sidebar.modules')}}</p>
      <ul>
        <li class="menu-item" :class="{ active: router.name === 'products' }" @click="router.push('/products')">
          <span class="icon">ICON</span>
          <span class="text">{{$t('sidebar.products')}}</span>
        </li>
        <li class="menu-item" :class="{ active: router.name === 'bundles' }" @click="router.push('/bundles')">
          <span class="icon">ICON</span>
          <span class="text">{{$t('sidebar.bundles')}}</span>
        </li>
        <li class="menu-item" :class="{ active: router.name === 'offers' }" @click="router.push('/offers')">
          <span class="icon">ICON</span>
          <span class="text">{{$t('sidebar.offers')}}</span>
        </li>
      </ul>
    </nav>


    <div class="theme-selector">
      <p>Paleta de colores:</p>
      <div class="previews">
        <div @click="setTheme('steam')" class="preview-dot steam" title="Steam Dark"></div>
        <div @click="setTheme('medical')" class="preview-dot medical" title="Medical Modern"></div>
        <div @click="setTheme('midnight')" class="preview-dot midnight" title="Midnight Violet"></div>
      </div>
    </div>


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
  sessionStorage.removeItem('token');
  router.push('/login');
};

const setTheme = (themeName) => {
  // Aplicamos el atributo al elemento raíz para que afecte a todos los componentes
  document.documentElement.setAttribute('data-theme', themeName);
  sessionStorage.setItem('preferred-theme', themeName);
};

import { useI18n } from 'vue-i18n'; // Importamos el hook

// Extraemos la variable 'locale' que controla el idioma global
const { locale } = useI18n();

const changeLanguage = (lang) => {
  locale.value = lang; // Cambia el idioma en toda la vista de inmediato
  localStorage.setItem('preferred-language', lang); // Lo guarda para la próxima visita
};

</script>

<style src="../styles/sidebar.css" scoped></style>
<style src="../styles/preview_dots.css" scoped></style>
<style scoped>
.language-switcher button {
background: transparent;
border: 1px solid var(--sidebar-border);
color: var(--text-main);
padding: 5px 10px;
cursor: pointer;
}
.language-switcher button.active {
background: var(--accent);
color: white;
}
</style>