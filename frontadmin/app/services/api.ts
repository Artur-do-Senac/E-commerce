'use client'

import axios from "axios"
import { store } from "../redux/store";
import { logout } from "../redux/slices/authSlice";

const api = axios.create({
    baseURL: 'http://localhost:8080'
});

// requisição: injeta o token que está na store do Redux
api.interceptors.request.use(
    (config) => {
        const token = store.getState().auth.token

        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    }
);

// resposta: 401 (token inválido/expirado) despacha o logout; sem usuário na store, o layout do sistema volta para o /login
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if(error.response?.status === 401){
            store.dispatch(logout());
        }

        return Promise.reject(error);
    }
);

export default api;

/*
tiramos as chamadas axios das páginas e as movemos para um service. O token passa a ficar somente na store do Redux e a ser enviado em toda requisição pelo interceptor. Resposta 401 despacha o logout.
*/
