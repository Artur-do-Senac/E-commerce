package com.example.ecommerceb2b.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime dataCadastro;

    // Quando o token deixa de valer (é a mesma data que vai dentro do JWT)
    private LocalDateTime dataExpiracao;

    // Token ativo: se estiver false ele continua salvo no banco, mas não é mais aceito
    @ColumnDefault("true")
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    public Token(String token, Usuario usuario, LocalDateTime dataExpiracao) {
        this.token = token;
        this.usuario = usuario;
        this.dataCadastro = LocalDateTime.now();
        this.dataExpiracao = dataExpiracao;
        this.ativo = true;
    }

    // Regra do domínio: o token só vale enquanto estiver ativo e dentro da validade
    public boolean estaValido() {
        return ativo && dataExpiracao != null && LocalDateTime.now().isBefore(dataExpiracao);
    }
}
