/*
import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
*/

import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // AGREGA ESTE BLOQUE DE SERVIDOR (SERVER)
  server: {
    proxy: {
      // Le decimos a Vite: Todo lo que empiece con /api, mándalo al puerto 2026
      '/api': {
        target: 'http://localhost:2026',
        //target: 'http://localhost:8000/index.php',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})
