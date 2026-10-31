import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../redux/slices/authSlice";
import produtoFiltroReducer from "../redux/slices/produtoFiltroSlice";


export const store = configureStore({
    reducer:{
        auth: authReducer,
        produtoFiltro: produtoFiltroReducer
    }
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;