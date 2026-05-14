import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
//The module to execute
import App from './bussiness/App.vue'
import router from './bussiness/router'
import i18n from './plugins/i18n' //import the variable, not the file
import './assets/themes.css'

const savedTheme = sessionStorage.getItem('preferred-theme') || 'steam';
document.documentElement.setAttribute('data-theme', savedTheme);

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)

app.mount('#app')

//if you want CLINIC TO ACTUALLY WORK USE THIS

/*
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
 */


