package com.example.ecommerceb2b.domain.entities;


import com.example.ecommerceb2b.application.DTOs.EmpresaRequest;
import com.example.ecommerceb2b.application.DTOs.EnumStatusEmpresa;
import com.example.ecommerceb2b.domain.valueobject.Telefone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String razaoSocial;
    private String cnpj;

    @OneToMany(mappedBy = "empresa")
    private List<Usuario> usuarios;



    @Embedded
    private Telefone telefone;
    private EnumStatusEmpresa status = EnumStatusEmpresa.ATIVO;

    // Ramo da empresa: várias empresas podem pertencer à mesma categoria
    @ManyToOne
    private Categoria categoria;

    // Regras do domínio: o que não for válido lança erro (a Controller transforma em 400 com a mensagem)
    public void setRazaoSocial(String razaoSocial) {
        if (razaoSocial == null || razaoSocial.isBlank()) {
            throw new IllegalArgumentException("Razao Social Invalida!");
        }
        this.razaoSocial = razaoSocial;
    }

    // O CNPJ precisa ter 14 dígitos (a máscara 00.000.000/0000-00 é aceita)
    public void setCnpj(String cnpj) {
        if (cnpj == null || cnpj.replaceAll("\\D", "").length() != 14) {
            throw new IllegalArgumentException("CNPJ Invalido!");
        }
        this.cnpj = cnpj;
    }

    // Toda empresa precisa pertencer a uma categoria (ramo)
    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria Invalida!");
        }
        this.categoria = categoria;
    }

    // O telefone é opcional: só é validado e guardado quando vier preenchido
    public void informarTelefone(String telefone) {
        if (telefone != null && !telefone.isBlank()) {
            this.setTelefone(new Telefone(telefone));
        }
    }

    public Empresa(EmpresaRequest empresa, Categoria categoria) {
        this.setRazaoSocial(empresa.razaoSocial());
        this.setCnpj(empresa.cnpj());
        this.informarTelefone(empresa.telefone());
        this.setCategoria(categoria);
    }
}
