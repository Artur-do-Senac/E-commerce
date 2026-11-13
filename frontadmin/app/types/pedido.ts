import { EnderecoUsuario } from "./usuario";

export class ItemPedido {

    constructor(
        public produtoId:number,
        public produtoNome:string,
        public empresaId:number | null,
        public empresaNome:string | null,
        public quantidade:number,
        public precoUnitario:number,
        public subtotal:number,
        public status:string
    ){

    }
}

// dados de contato do cliente que fez o pedido (o endereço vem do cadastro dele, preenchido com o ViaCEP)
export interface ClientePedido {
    nome:string
    email:string
    telefone:string | null
    endereco:EnderecoUsuario | null
}

export class Pedido {

    constructor(
        public id:number | null,
        public valorTotal:number,
        public data:string,
        public status:string,
        public cliente:ClientePedido | null,
        public itens:ItemPedido[]
    ){

    }
}

// o que o back recebe para criar um pedido: só o produto e a quantidade (o valor total é calculado lá)
export interface ItemPedidoRequest{
    produtoId:number
    quantidade:number
}
