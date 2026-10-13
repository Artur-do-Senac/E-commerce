package com.example.ecommerceb2b.domain.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String razaoSocial;
    public String cnpj;
    private EnumStatusEmpresa status = EnumStatusEmpresa.ATIVO;

    // Ramo da empresa: várias empresas podem pertencer à mesma categoria
    @ManyToOne
    private Categoria categoria;
}
