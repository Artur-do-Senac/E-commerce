package com.example.ecommerceb2b.domain.entities;


import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import com.example.ecommerceb2b.application.DTOs.PedidoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private BigDecimal valorTotal;
    private LocalDateTime data;
    private BigDecimal desconto;
    private EnumStatusPedido status = EnumStatusPedido.PENDENTE;

    // Empresa dona do pedido: é ela que define quem consegue enxergar e mexer nele
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;

    // Produtos do pedido: um pedido tem vários produtos e um produto pode estar em vários pedidos
    @ManyToMany
    @JoinTable(
            name = "tb_pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    @ToString.Exclude
    private Set<Produto> produtos = new HashSet<>();

    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
    }

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
