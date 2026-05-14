
import { createI18n } from 'vue-i18n'
import es from '../locals/spanish.json'
import en from '../locals/english.json'

// Get preffered lan
const savedLanguage = localStorage.getItem('preferred-language') || 'es';

const i18n = createI18n({
    legacy: false, // For use in <script setup> (Composition API)!
    locale: savedLanguage, // The actual languaje
    fallbackLocale: 'es', // for missing translaction use this one
    messages: {
        es,
        en
    }
})

export default i18n