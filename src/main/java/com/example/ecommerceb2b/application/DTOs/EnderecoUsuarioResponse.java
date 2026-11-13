package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.valueobject.Endereco;

public record EnderecoUsuarioResponse(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {

    public EnderecoUsuarioResponse(Endereco endereco) {

      this(
        endereco.getCep(),
        endereco.getLogradouro(),
        endereco.getNumero(),
        endereco.getComplemento(),
        endereco.getBairro(),
        endereco.getCidade(),
        endereco.getUf()
        );
    }
}
