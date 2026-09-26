export class Pedido {

    constructor(
        public id:number | null,
        public valorTotal:number,
        public data:string,
        public desconto:number,
        public status:string
    ){

    }
}

export interface PedidoFormProp{
    pedidoExistente?:Pedido
}
