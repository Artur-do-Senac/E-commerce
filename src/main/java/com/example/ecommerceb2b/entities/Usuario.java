package com.example.ecommerceb2b.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Usuario {

    public Long id;
    public String nome;
    public String cpf;
    public String senha;
    public String email;
}
