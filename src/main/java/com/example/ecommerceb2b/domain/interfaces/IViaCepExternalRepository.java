package com.example.ecommerceb2b.domain.interfaces;

import com.example.ecommerceb2b.infra.dto.ViaCepResponse;

// Porta: o domínio/aplicação só conhecem esta interface, quem fala com o ViaCEP é a implementação na infra
public interface IViaCepExternalRepository {

    ViaCepResponse buscarEnderecoViaCepPorCep(String cep);
}
