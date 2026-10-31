import { Pedido } from "../types/pedido";
import api from "./api";

export async function buscarListaPedidos() : Promise<Pedido[]> {

    const dados = await api.get<Pedido[]>('/pedidos');

    if(dados.status == 200){
        return dados.data;
    }

    return [];

}

export async function deletarPedido (id:number) : Promise<number> {

    const dados = await api.delete('/pedidos/'+id+'/excluir');
    return dados.status;
}

export async function consultaPedidoPorId(id:number) : Promise<Pedido>{
    const valorPedidoBack = await api.get<Pedido>('/pedidos/'+id);

    return valorPedidoBack.data

}

export async function atualizarPedido(pedido:Pedido) : Promise<number>{
    const dadosRetorno = await api.put<number>('/pedidos/'+pedido.id, pedido);
    return dadosRetorno.data;
}

export async function salvarPedido(pedido:Pedido) : Promise<number>{
    const dadosRetorno = await api.post<number>('/pedidos', pedido);
    return dadosRetorno.data;
}
