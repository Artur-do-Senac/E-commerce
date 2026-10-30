import { Pedido } from "../types/pedido";
import api from "./api";

export async function buscarListaPedidos() : Promise<Pedido[]> {

    const dados = await api.get<Pedido[]>('http://localhost:8080/pedidos');

    if(dados.status == 200){
        return dados.data;
    }

    return [];

}

export async function deletarPedido (id:number) : Promise<number> {

    const dados = await api.delete('http://localhost:8080/pedidos/'+id+'/excluir');
    return dados.status;
}

export async function consultaPedidoPorId(id:number) : Promise<Pedido>{
    const valorPedidoBack = await api.get<Pedido>('http://localhost:8080/pedidos/'+id);

    return valorPedidoBack.data

}

export async function atualizarPedido(pedido:Pedido) : Promise<number>{
    const dadosRetorno = await api.put<number>('http://localhost:8080/pedidos/'+pedido.id, pedido);
    return dadosRetorno.data;
}

export async function salvarPedido(pedido:Pedido) : Promise<number>{
    const dadosRetorno = await api.post<number>('http://localhost:8080/pedidos', pedido);
    return dadosRetorno.data;
}
