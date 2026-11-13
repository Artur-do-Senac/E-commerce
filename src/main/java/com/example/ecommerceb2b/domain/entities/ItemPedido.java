package com.example.ecommerceb2b.domain.entities;

import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

// Item de um pedido: um produto com a quantidade pedida e o preço da hora da compra
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
    @ToString.Exclude
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
    private Produto produto;

    private Integer quantidade;

    // Preço do produto no momento do pedido (se o produto mudar de preço depois, o pedido não muda)
    private BigDecimal precoUnitario;

    // Status por empresa: cada empresa conclui os itens dos produtos dela (PENDENTE até a empresa concluir)
    private EnumStatusPedido status = EnumStatusPedido.PENDENTE;

    // Regra do domínio: a quantidade precisa ser maior que zero (a Controller transforma o erro em 400 com a mensagem)
    public void setQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade < 1) {
            throw new IllegalArgumentException("Quantidade Invalida!");
        }
        this.quantidade = quantidade;
    }

    public ItemPedido(Produto produto, Integer quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto Invalido!");
        }
        if (quantidade != null && quantidade > produto.getQuantidadeEstoque()) {
            throw new IllegalArgumentException("Estoque insuficiente para " + produto.getNome() + "!");
        }
        this.produto = produto;
        this.setQuantidade(quantidade);
        this.precoUnitario = BigDecimal.valueOf(produto.getPreco());
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // O produto do item é de qual empresa? (usado para o GERENTE enxergar só os itens dele)
    public boolean pertenceA(Long empresaId) {
        return produto.getEmpresa() != null && produto.getEmpresa().getId().equals(empresaId);
    }
}
