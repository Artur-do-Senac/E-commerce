export class Empresa {

    constructor(
        public id:number | null,
        public razaoSocial:string,
        public cnpj:string,
        public status:string
    ){

    }
}

export interface EmpresaFormProp{
    empresaExistente?:Empresa
}
