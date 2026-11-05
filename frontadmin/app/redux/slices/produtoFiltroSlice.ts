import { ProdutoFiltroState } from "@/app/types/produtoFiltro";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";


// busca, filtro de status e filtro de empresa (só o ADMIN usa) da listagem de produtos: ficam na store, então continuam ao ir para o editar e voltar
const initialState: ProdutoFiltroState = {
    busca: "",
    status: "TODOS",
    empresa: "TODAS"
}


const produtoFiltroSlice = createSlice({
    name: 'produtoFiltro',
    initialState,
    reducers:{
        definirBusca:(state, action:PayloadAction<string>) => {
            state.busca = action.payload;
        },
        definirStatus:(state, action:PayloadAction<string>) => {
            state.status = action.payload;
        },
        definirEmpresa:(state, action:PayloadAction<string>) => {
            state.empresa = action.payload;
        }
    }
});

export const {definirBusca, definirStatus, definirEmpresa} = produtoFiltroSlice.actions;

export default produtoFiltroSlice.reducer;
