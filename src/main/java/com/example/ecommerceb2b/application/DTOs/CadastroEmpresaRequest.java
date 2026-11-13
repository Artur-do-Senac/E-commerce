package com.example.ecommerceb2b.application.DTOs;

// Cadastro público (sem token) de uma empresa: cria a empresa e o usuário GERENTE dela
// Os cinco primeiros campos são do gerente, os demais são da empresa
public record CadastroEmpresaRequest(String nome, String email, String senha, String cpf, String telefone,
                                     String razaoSocial, String cnpj, String telefoneEmpresa, Long categoriaId) {
}
