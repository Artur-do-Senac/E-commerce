package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.ItemPedido;
import com.example.ecommerceb2b.domain.entities.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(Long id, BigDecimal valorTotal, LocalDateTime data, EnumStatusPedido status, ClientePedidoResponse cliente, List<ItemPedidoResponse> itens) {

    // Pedido completo (todos os itens), como o ADMIN enxerga
    public PedidoResponse(Pedido pedidoEntidade) {
        this(pedidoEntidade, null);
    }

    // Visão de uma empresa: só os itens dos produtos dela, o valor total calculado somente com eles e o status só desses itens (empresaId null = todos os itens)
    public PedidoResponse(Pedido pedidoEntidade, Long empresaId) {

      this(
        pedidoEntidade.getId(),
        total(itensVisiveis(pedidoEntidade, empresaId)),
        pedidoEntidade.getData(),
        status(pedidoEntidade, itensVisiveis(pedidoEntidade, empresaId)),
        pedidoEntidade.getCliente() == null ? null : new ClientePedidoResponse(pedidoEntidade.getCliente()),
        itensVisiveis(pedidoEntidade, empresaId).stream().map(ItemPedidoResponse::new).toList()
        );
    }

    private static List<ItemPedido> itensVisiveis(Pedido pedido, Long empresaId) {

        return pedido.getItens().stream()
                .filter(item -> empresaId == null || item.pertenceA(empresaId))
                .toList();
    }

    private static BigDecimal total(List<ItemPedido> itens) {

        return itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // O pedido cancelado continua cancelado. Fora isso, está concluído quando todos os itens visíveis foram concluídos (cada empresa conclui os seus)
    private static EnumStatusPedido status(Pedido pedido, List<ItemPedido> itens) {

        if (pedido.getStatus() == EnumStatusPedido.CANCELADO) {
            return EnumStatusPedido.CANCELADO;
        }

        boolean todosConcluidos = !itens.isEmpty() && itens.stream().allMatch(item -> item.getStatus() == EnumStatusPedido.CONCLUIDO);

        return todosConcluidos ? EnumStatusPedido.CONCLUIDO : EnumStatusPedido.PENDENTE;
    }
}
