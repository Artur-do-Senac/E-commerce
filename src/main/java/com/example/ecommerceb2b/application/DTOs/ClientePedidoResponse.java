package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Usuario;

// Dados de contato do cliente que fez o pedido, para a empresa conseguir entregar
public record ClientePedidoResponse(String nome, String email, String telefone, EnderecoUsuarioResponse endereco) {

    public ClientePedidoResponse(Usuario cliente) {

      this(
        cliente.getNome(),
        cliente.getEmail(),
        cliente.getTelefone() == null ? null : cliente.getTelefone().toString(),
        cliente.getEndereco() == null ? null : new EnderecoUsuarioResponse(cliente.getEndereco())
        );
    }
}
