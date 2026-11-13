package com.example.ecommerceb2b.application.DTOs;

// Endereço que o cliente informa para completar o cadastro (o CEP é consultado no ViaCEP pelo GET /endereco/{cep})
public record EnderecoUsuarioRequest(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf) {
}
