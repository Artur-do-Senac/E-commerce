import { Empresa } from "../types/empresa";
import api from "./api";

// o backend recebe só o id da categoria (categoriaId), e não o objeto inteiro
export type EmpresaRequest = Empresa & { categoriaId: number | null };

export async function buscarListaEmpresas() : Promise<Empresa[]> {

    const dados = await api.get<Empresa[]>('http://localhost:8080/empresas');

    if(dados.status == 200){
        return dados.data;
    }

    return [];

}

export async function deletarEmpresa (id:number) : Promise<number> {

    const dados = await api.delete('http://localhost:8080/empresas/'+id+'/excluir');
    return dados.status;
}

export async function consultaEmpresaPorId(id:number) : Promise<Empresa>{
    const valorEmpresaBack = await api.get<Empresa>('http://localhost:8080/empresas/'+id);

    return valorEmpresaBack.data

}

export async function atualizarEmpresa(empresa:EmpresaRequest) : Promise<number>{
    const dadosRetorno = await api.put<number>('http://localhost:8080/empresas/'+empresa.id, empresa);
    return dadosRetorno.data;
}

export async function salvarEmpresa(empresa:EmpresaRequest) : Promise<number>{
    const dadosRetorno = await api.post<number>('http://localhost:8080/empresas', empresa);
    return dadosRetorno.data;
}
