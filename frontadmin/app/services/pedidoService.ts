import { ItemPedidoRequest, Pedido } from "../types/pedido";
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

// o pedido é criado a partir dos itens do carrinho (produto + quantidade), o valor total é calculado no back
export async function salvarPedido(itens:ItemPedidoRequest[]) : Promise<Pedido>{
    const dadosRetorno = await api.post<Pedido>('/pedidos', { itens });
    return dadosRetorno.data;
}


// o GERENTE conclui os itens dos produtos da empresa dele no pedido (o estoque desses produtos é baixado no back)
export async function concluirItensPedido(id:number) : Promise<number>{
    const dados = await api.patch('/pedidos/'+id+'/concluir');
    return dados.status;
}
