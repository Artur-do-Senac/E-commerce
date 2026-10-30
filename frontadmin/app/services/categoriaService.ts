import { Categoria } from "../types/categoria";
import api from "./api";

export async function buscarListaCategorias() : Promise<Categoria[]> {

    const dados = await api.get<Categoria[]>('http://localhost:8080/categorias');

    if(dados.status == 200){
        return dados.data;
    }

    return [];

}

export async function deletarCategoria (id:number) : Promise<number> {

    const dados = await api.delete('http://localhost:8080/categorias/'+id+'/excluir');
    return dados.status;
}

export async function consultaCategoriaPorId(id:number) : Promise<Categoria>{
    const valorCategoriaBack = await api.get<Categoria>('http://localhost:8080/categorias/'+id);

    return valorCategoriaBack.data

}

export async function atualizarCategoria(categoria:Categoria) : Promise<number>{
    const dadosRetorno = await api.put<number>('http://localhost:8080/categorias/'+categoria.id, categoria);
    return dadosRetorno.data;
}

export async function salvarCategoria(categoria:Categoria) : Promise<number>{
    const dadosRetorno = await api.post<number>('http://localhost:8080/categorias', categoria);
    return dadosRetorno.data;
}
