import { ProdutoFiltroState } from "@/app/types/produtoFiltro";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";


// busca e filtro de status da listagem de produtos: ficam na store, então continuam ao ir para o editar e voltar
const initialState: ProdutoFiltroState = {
    busca: "",
    status: "TODOS"
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
        }
    }
});

export const {definirBusca, definirStatus} = produtoFiltroSlice.actions;

export default produtoFiltroSlice.reducer;
