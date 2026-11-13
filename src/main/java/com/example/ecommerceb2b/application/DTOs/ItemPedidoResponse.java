package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponse(Long produtoId, String produtoNome, Long empresaId, String empresaNome, Integer quantidade, BigDecimal precoUnitario, BigDecimal subtotal, EnumStatusPedido status) {

    public ItemPedidoResponse(ItemPedido item) {

      this(
        item.getProduto().getId(),
        item.getProduto().getNome(),
        item.getProduto().getEmpresa() == null ? null : item.getProduto().getEmpresa().getId(),
        item.getProduto().getEmpresa() == null ? null : item.getProduto().getEmpresa().getRazaoSocial(),
        item.getQuantidade(),
        item.getPrecoUnitario(),
        item.getSubtotal(),
        item.getStatus()
        );
    }
}
