import { Categoria } from "./categoria";

export class Empresa {

    constructor(
        public id:number | null,
        public razaoSocial:string,
        public cnpj:string,
        public status:string,
        public categoria:Categoria | null
    ){

    }
}

export interface EmpresaFormProp{
    empresaExistente?:Empresa
}
