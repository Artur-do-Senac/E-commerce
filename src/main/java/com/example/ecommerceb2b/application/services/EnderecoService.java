package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.EnderecoResponse;
import com.example.ecommerceb2b.domain.interfaces.IViaCepExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de Application: consulta o endereço pela porta (interface) e devolve só o que a API expõe (EnderecoResponse)
// A EnderecoController só chama os métodos daqui.
@Service
public class EnderecoService {

    @Autowired
    private IViaCepExternalRepository viaCepExternalRepository;

    // Busca o endereço de um CEP. CEP inexistente ou falha na consulta lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public EnderecoResponse buscarEndereco(String cep) {

        var enderecoCompleto = viaCepExternalRepository.buscarEnderecoViaCepPorCep(cep);

        return new EnderecoResponse(enderecoCompleto.getCep(),
                enderecoCompleto.getLogradouro(),
                enderecoCompleto.getBairro(),
                enderecoCompleto.getLocalidade(),
                enderecoCompleto.getUf());
    }
}
