import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
//The module to execute
import App from './bussiness/App.vue'
import router from './bussiness/router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

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

