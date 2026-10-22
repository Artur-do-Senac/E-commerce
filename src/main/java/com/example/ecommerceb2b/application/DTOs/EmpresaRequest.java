package com.example.ecommerceb2b.application.DTOs;

public record EmpresaRequest(String razaoSocial, String cnpj, String telefone, Long categoriaId) {
}
