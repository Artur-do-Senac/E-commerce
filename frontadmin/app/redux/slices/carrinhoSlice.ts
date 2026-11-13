import { CarrinhoState, ItemCarrinho } from "@/app/types/carrinho";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { logout } from "./authSlice";


// carrinho do cliente: fica na store enquanto ele navega e é esvaziado ao confirmar o pedido ou ao sair
const initialState: CarrinhoState = {
    itens: []
}


const carrinhoSlice = createSlice({
    name: 'carrinho',
    initialState,
    reducers:{
        // produto novo entra com 1 unidade; se já está no carrinho soma 1 (sem passar do estoque)
        adicionar:(state, action:PayloadAction<Omit<ItemCarrinho, 'quantidade'>>) => {
            const existente = state.itens.find(item => item.produtoId === action.payload.produtoId);

            if (existente) {
                existente.quantidade = Math.min(existente.quantidade + 1, existente.estoque);
            } else {
                state.itens.push({...action.payload, quantidade: 1});
            }
        },
        alterarQuantidade:(state, action:PayloadAction<{produtoId:number, quantidade:number}>) => {
            const existente = state.itens.find(item => item.produtoId === action.payload.produtoId);

            if (existente) {
                existente.quantidade = Math.max(1, Math.min(action.payload.quantidade, existente.estoque));
            }
        },
        remover:(state, action:PayloadAction<number>) => {
            state.itens = state.itens.filter(item => item.produtoId !== action.payload);
        },
        limpar:(state) => {
            state.itens = [];
        }
    },
    extraReducers:(builder) => {
        builder.addCase(logout, (state) => {
            state.itens = [];
        });
    }
});

export const {adicionar, alterarQuantidade, remover, limpar} = carrinhoSlice.actions;

export default carrinhoSlice.reducer;
