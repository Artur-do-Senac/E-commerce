package com.example.ecommerceb2b.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

// Formato da resposta do ViaCEP (https://viacep.com.br/ws/{cep}/json/). Para CEP inexistente ele devolve {"erro": true}
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepResponse {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String estado;
    private Boolean erro;
}
