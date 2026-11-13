export interface EnderecoUsuario {
    cep:string
    logradouro:string
    numero:string
    complemento:string | null
    bairro:string
    cidade:string
    uf:string
}

// o que a consulta de CEP (ViaCEP, pelo back) devolve
export interface EnderecoCep {
    cep:string
    logradouro:string
    bairro:string
    cidade:string
    uf:string
}

export class Usuario {

    constructor(
        public id:number | null,
        public nome:string,
        public email:string,
        public status:string,
        public cpf:string,
        public senha:string,
        public empresaId:number | null,
        public perfil:string,
        public empresaNome?:string | null,
        public endereco?:EnderecoUsuario | null
    ){

    }
}

export interface UsuarioFormProp{
    usuarioExistente?:Usuario
}
