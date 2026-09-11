export class Produto {

    constructor(
        public id:number | null,
        public nome:string,
        public preco:number,
        public vencimento:string,
        public quantidadeEstoque:number,
        public status:string
    ){

    }
}
