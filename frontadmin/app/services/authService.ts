import { CadastroEmpresaRequest, CadastroUsuarioRequest, LoginResponse } from "../types/auth";
import api from "./api";


export async function loginBack(email:string, senha:string) : Promise<LoginResponse>{
    
    var loginResposta = await api.post<LoginResponse>("/auth/login", {email: email, senha: senha});

    if (loginResposta.status != 200){
        alert("Usuário e/ou senha inválidos!")}


    return loginResposta.data;
}

export async function cadastrarUsuario(cadastro:CadastroUsuarioRequest) : Promise<void>{

    await api.post("/auth/cadastro", cadastro);
}

export async function cadastrarEmpresa(cadastro:CadastroEmpresaRequest) : Promise<void>{

    await api.post("/auth/cadastro-empresa", cadastro);
}
