import { Usuario } from "../types/usuario";
import api from "./api";

export async function buscarListaUsuarios() : Promise<Usuario[]> {

    const dados = await api.get<Usuario[]>('/usuarios');

    if(dados.status == 200){
        return dados.data;
    }

    return [];
    
}

export async function deletarUsuario (id:number) : Promise<number> {

    var dados = await api.delete('/usuarios/'+id+'/excluir');
    return dados.status;
}

export async function atualizarStatusUsuario(usuario:Usuario) : Promise<void>{
    const novoStatus = {
        statusUsuario: usuario.status === "ATIVO" ? "INATIVO" : "ATIVO"
    };

    try {
        await api.patch('/usuarios/'+usuario.id+'/status', novoStatus)
        alert("Status atualizado com sucesso!");
    } catch (error) {
        alert("Erro ao atualizar status");
        return;
    }

}

export async function consultaUsuarioPorId(id:number) : Promise<Usuario>{
    const valorUsuarioBack = await api.get<Usuario>('/usuarios/'+id);

    return valorUsuarioBack.data

}

export async function atualizarUsuario(usuario:Usuario) : Promise<number>{
    var dadosRetorno = await api.put<number>('/usuarios/'+usuario.id, usuario);
    return dadosRetorno.data;
}

export async function salvarUsuario(usuario:Usuario) : Promise<number>{
    var dadosRetorno = await api.post<number>('/usuarios', usuario);
    return dadosRetorno.data;
}

export async function buscarUsuarioLogado() : Promise<Usuario>{

    return (await api.get<Usuario>('/usuarios/buscarusuariologado')).data;
}