
export const ENDPOINTS = {
    auth: {
        login: '/api/auth/login',
    },
    products: {
        products: '/api/pos/cart/products',
        cartSummary: '/api/pos/cart/summary', // Asumiendo un endpoint para el total actual
        addToCart: '/api/pos/cart/add'
    },
    navigation: {
        //home: '/',
        bundles: '/bundles',
        offers: '/offers'
    }
};