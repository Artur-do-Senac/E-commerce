package com.example.ecommerceb2b.domain.entities;


import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import com.example.ecommerceb2b.application.DTOs.PedidoRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal valorTotal;
    private LocalDateTime data;
    private BigDecimal desconto;
    private EnumStatusPedido status = EnumStatusPedido.PENDENTE;

    // Regras do domínio: o que não for válido lança erro (a Controller transforma em 400 com a mensagem)
    public void setValorTotal(BigDecimal valorTotal) {
        if (valorTotal == null || valorTotal.signum() < 0) {
            throw new IllegalArgumentException("Valor Total Invalido!");
        }
        this.valorTotal = valorTotal;
    }

    // O desconto não pode ser negativo nem maior que o valor total (por isso o valor total é definido antes)
    public void setDesconto(BigDecimal desconto) {
        if (desconto == null || desconto.signum() < 0 || (valorTotal != null && desconto.compareTo(valorTotal) > 0)) {
            throw new IllegalArgumentException("Desconto Invalido!");
        }
        this.desconto = desconto;
    }

    public void setData(LocalDateTime data) {
        if (data == null) {
            throw new IllegalArgumentException("Data Invalida!");
        }
        this.data = data;
    }

    public Pedido(PedidoRequest pedido) {
        this.setValorTotal(pedido.valorTotal());
        this.setDesconto(pedido.desconto());
        this.setData(pedido.data());
    }
}
