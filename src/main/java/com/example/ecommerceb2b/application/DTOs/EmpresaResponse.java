package com.example.ecommerceb2b.application.DTOs;

import com.example.ecommerceb2b.domain.entities.Empresa;

public record EmpresaResponse(Long id, String razaoSocial, String cnpj, String telefone, EnumStatusEmpresa status, CategoriaResponse categoria) {

    public EmpresaResponse(Empresa empresaEntidade) {

      this(
        empresaEntidade.getId(),
        empresaEntidade.getRazaoSocial(),
        empresaEntidade.getCnpj(),
        empresaEntidade.getTelefone() == null ? null : empresaEntidade.getTelefone().toString(),
        empresaEntidade.getStatus(),
        empresaEntidade.getCategoria() == null ? null : new CategoriaResponse(empresaEntidade.getCategoria())
        );
    }
}
