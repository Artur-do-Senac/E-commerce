import { Usuario } from "./usuario"

export interface LoginResponse{
    token:string
}

export interface AuthState {
    usuario: Usuario|null
    token : string

}

export interface CadastroUsuarioRequest{
    nome:string
    email:string
    senha:string
    cpf:string
    telefone:string
}

export interface CadastroEmpresaRequest extends CadastroUsuarioRequest{
    razaoSocial:string
    cnpj:string
    telefoneEmpresa:string
    categoriaId:number | null
}
