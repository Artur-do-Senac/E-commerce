package com.example.ecommerceb2b.application.DTOs;

import java.util.List;

// O pedido é feito de itens (produto + quantidade). O valor total é calculado pelo back
public record PedidoRequest(List<ItemPedidoRequest> itens) {
}
