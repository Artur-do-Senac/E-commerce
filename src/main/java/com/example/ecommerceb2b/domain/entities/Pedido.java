package com.example.ecommerceb2b.domain.entities;


import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private EnumStatusPedido status = EnumStatusPedido.PENDENTE;

    // Cliente que fez o pedido (o usuário logado na hora da compra)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Usuario cliente;

    // Itens do pedido: um pedido tem vários itens e cada item é de um produto (de qualquer empresa)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
        this.valorTotal = this.itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // A empresa conclui os itens dos produtos dela: o estoque desses produtos é baixado (primeiro confere tudo, para não baixar só parte)
    // Sem item pendente da empresa ou sem estoque suficiente lança erro (a Controller transforma em 400 com a mensagem)
    public void concluirItensDaEmpresa(Long empresaId) {

        List<ItemPedido> pendentes = this.itens.stream()
                .filter(item -> item.pertenceA(empresaId) && item.getStatus() == EnumStatusPedido.PENDENTE)
                .toList();

        if (pendentes.isEmpty()) {
            throw new IllegalArgumentException("Nenhum item pendente da sua empresa neste pedido!");
        }

        pendentes.forEach(item -> {
            if (item.getQuantidade() > item.getProduto().getQuantidadeEstoque()) {
                throw new IllegalArgumentException("Estoque insuficiente para " + item.getProduto().getNome() + "!");
            }
        });

        pendentes.forEach(item -> {
            item.getProduto().baixarEstoque(item.getQuantidade());
            item.setStatus(EnumStatusPedido.CONCLUIDO);
        });

        // Quando todas as empresas concluíram os seus itens, o pedido todo está concluído
        if (this.status != EnumStatusPedido.CANCELADO && this.itens.stream().allMatch(item -> item.getStatus() == EnumStatusPedido.CONCLUIDO)) {
            this.status = EnumStatusPedido.CONCLUIDO;
        }
    }

    // Regra do domínio: o pedido precisa ter pelo menos um item (a Controller transforma o erro em 400 com a mensagem)
    public Pedido(Usuario cliente, List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Itens Invalidos!");
        }
        this.cliente = cliente;
        this.data = LocalDateTime.now();
        itens.forEach(this::adicionarItem);
    }
}
