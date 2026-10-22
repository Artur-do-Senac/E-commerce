package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponse(Long id, BigDecimal valorTotal, LocalDateTime data, BigDecimal desconto, EnumStatusPedido status) {

    public PedidoResponse(Pedido pedidoEntidade) {

      this(
        pedidoEntidade.getId(),
        pedidoEntidade.getValorTotal(),
        pedidoEntidade.getData(),
        pedidoEntidade.getDesconto(),
        pedidoEntidade.getStatus()
        );
    }
}
