/*
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:2026/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// === INTERCEPTOR DE PETICIONES ===
api.interceptors.request.use(
    (config) => {
        // 1. Buscamos el token en el almacenamiento local del navegador
        const token = localStorage.getItem('token');

        // 2. Si existe, lo adjuntamos a la cabecera 'Authorization'
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;*/

import axios from 'axios';

const api = axios.create({
    // Ya no ponemos "http://localhost:2026".
    // Al dejar solo '/api', Axios le pedirá los datos a Vite.
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;
