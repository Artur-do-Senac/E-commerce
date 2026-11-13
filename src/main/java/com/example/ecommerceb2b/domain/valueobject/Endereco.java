package com.example.ecommerceb2b.domain.valueobject;

import lombok.Getter;

// Endereço de entrega do cliente (o CEP, a rua, o bairro, a cidade e a UF normalmente vêm da consulta ao ViaCEP)
@Getter
public class Endereco {

    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    public Endereco(){
    }

    // Regras do domínio: o que não for válido lança erro (a Controller transforma em 400 com a mensagem)
    public Endereco(String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String uf){

        if (cep == null || cep.replaceAll("\\D", "").length() != 8) {
            throw new IllegalArgumentException("CEP Invalido!");
        }
        if (logradouro == null || logradouro.isBlank()) {
            throw new IllegalArgumentException("Logradouro Invalido!");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Numero Invalido!");
        }
        if (bairro == null || bairro.isBlank()) {
            throw new IllegalArgumentException("Bairro Invalido!");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("Cidade Invalida!");
        }
        if (uf == null || !uf.trim().matches("[A-Za-z]{2}")) {
            throw new IllegalArgumentException("UF Invalida!");
        }

        this.cep = cep.replaceAll("\\D", "").replaceAll("(\\d{5})(\\d{3})", "$1-$2");
        this.logradouro = logradouro.trim();
        this.numero = numero.trim();
        this.complemento = complemento == null ? null : complemento.trim();
        this.bairro = bairro.trim();
        this.cidade = cidade.trim();
        this.uf = uf.trim().toUpperCase();
    }
}
