'use client'

import axios from "axios"
import Cookies from "js-cookie";

const api = axios.create({
    baseURL: 'http://localhost:8080'
});

api.interceptors.request.use(
    (config) => {
        const token = Cookies.get('token')

        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    }
);

export default api;

/*
tiramos as chamadas axios das páginas e as movemos para um service. O token passa a ficar num cookie e a ser enviado em toda requisição.
*/