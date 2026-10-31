import { AuthState } from "@/app/types/auth";
import { Usuario } from "@/app/types/usuario";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";


// token e usuário ficam somente na store (sem cookie/localStorage): recarregar a página desloga
const initialState: AuthState = {
    usuario: null,
    token : ""
}


const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers:{
        login:(state, action:PayloadAction<{token:string, usuario:Usuario | null}>) => {

            state.token = action.payload.token;
            state.usuario = action.payload.usuario

        },
        logout:(state) => {

            state.token = "";
            state.usuario = null;

        }
    }
});

export const {login, logout} = authSlice.actions;

export default authSlice.reducer;
