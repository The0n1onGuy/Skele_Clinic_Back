import axios from 'axios';

const mode = import.meta.env.VITE_API_MODE; // PHP o DIRECT
const baseURL = mode === 'PHP'
    ? import.meta.env.VITE_URL_PHP
    : import.meta.env.VITE_URL_DIRECT;

const api = axios.create({
    // Ya no ponemos "http://localhost:2026".
    // Al dejar solo '/api', Axios le pedirá los datos a Vite.

    //baseURL: '/api',
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json'
    }
});

api.buildUrl = (path, action) => {
    if (mode === 'PHP') {
        return ''; // To where its pointing at the BaseURL
    }
    return path; // Return the full route
};

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