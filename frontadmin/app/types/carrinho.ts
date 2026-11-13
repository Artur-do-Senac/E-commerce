export interface ItemCarrinho {
    produtoId: number
    nome: string
    preco: number
    empresaId: number | null
    empresaNome: string | null
    estoque: number
    quantidade: number
}

export interface CarrinhoState {
    itens: ItemCarrinho[]
}
