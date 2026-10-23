package com.example.ecommerceb2b.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ecommerceb2b.application.DTOs.EnumStatusUsuario;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.domain.entities.Token;
import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${spring.secret}")
    private String secret;

    @Value("${spring.expiration}")
    private Long expiracao;

    @Value("${spring.emissor}")
    private String emissor;

    @Autowired
    private TokenRepository tokenRepository;


    // Gera o JWT do usuário que acabou de se autenticar e salva o token no banco (é isso que permite validar e invalidar depois)
    public String gerarToken(Usuario usuario){

        Algorithm algorithm = Algorithm.HMAC256(secret);

        // A data de expiração é calculada uma vez só: a mesma vai para dentro do JWT e para a tabela token
        var dataExpiracao = LocalDateTime.now().plusMinutes(expiracao);

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario.getEmail())
                // Id único: sem ele, dois logins no mesmo segundo gerariam exatamente o mesmo token
                .withJWTId(UUID.randomUUID().toString())
                .withExpiresAt(dataExpiracao.atZone(ZoneId.systemDefault()).toInstant())
                .sign(algorithm);

        tokenRepository.save(new Token(token, usuario, dataExpiracao));

        return token;
    }

    // Valida o token em 3 etapas e devolve o usuário logado (com as roles que o JwtFilter usa para liberar os endpoints)
    // Qualquer problema lança exceção, e o JwtFilter transforma isso em 401
    public UsuarioLogado verificadorToken(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algorithm).withIssuer(emissor).build();

        // 1) Assinatura, emissor e validade do próprio JWT
        verificador.verify(token);

        // 2) O token precisa existir no banco, estar ativo (sem logout) e dentro da validade
        var tokenBanco = tokenRepository.findByToken(token).orElse(null);

        if(tokenBanco == null || !tokenBanco.estaValido()){
            throw new IllegalArgumentException("Token inválido!");
        }

        // 3) O dono do token não pode ter sido excluído depois do login (as roles são lidas do banco a cada requisição)
        var usuario = tokenBanco.getUsuario();

        if(usuario == null || usuario.getStatus() == EnumStatusUsuario.EXCLUIDO){
            throw new IllegalArgumentException("Token inválido!");
        }

        return new UsuarioLogado(usuario);
    }

    // Logout: o token continua no banco, mas marcado como inativo, então o JwtFilter passa a recusá-lo
    public void invalidarToken(String token){

        tokenRepository.findByToken(token).ifPresent(tokenBanco -> {
            tokenBanco.invalidar();
            tokenRepository.save(tokenBanco);
        });
    }

}
