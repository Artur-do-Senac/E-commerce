import { Produto } from "../types/produto";
import api from "./api";

export async function buscarListaProdutos() : Promise<Produto[]> {

    const dados = await api.get<Produto[]>('http://localhost:8080/produtos');

    if(dados.status == 200){
        return dados.data;
    }

    return [];

}

export async function deletarProduto (id:number) : Promise<number> {

    const dados = await api.delete('http://localhost:8080/produtos/'+id+'/excluir');
    return dados.status;
}

export async function consultaProdutoPorId(id:number) : Promise<Produto>{
    const valorProdutoBack = await api.get<Produto>('http://localhost:8080/produtos/'+id);

    return valorProdutoBack.data

}

export async function atualizarProduto(produto:Produto) : Promise<number>{
    const dadosRetorno = await api.put<number>('http://localhost:8080/produtos/'+produto.id, produto);
    return dadosRetorno.data;
}

export async function salvarProduto(produto:Produto) : Promise<number>{
    const dadosRetorno = await api.post<number>('http://localhost:8080/produtos', produto);
    return dadosRetorno.data;
}
