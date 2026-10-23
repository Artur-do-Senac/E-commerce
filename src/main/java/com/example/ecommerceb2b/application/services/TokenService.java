package com.example.ecommerceb2b.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.domain.entities.Token;
import com.example.ecommerceb2b.domain.repository.TokenRepository;
import com.example.ecommerceb2b.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${spring.secret}")
    private String secret;

    @Value("${spring.expiration}")
    private Long expiracao;

    @Value("${spring.emissor}")
    private String emissor;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRepository tokenRepository;


    public String gerarToken(String subject){

        Algorithm algorithm = Algorithm.HMAC256(secret);

        var usuario = usuarioRepository.findAll().stream().filter(u -> u.getEmail().equals(subject)).findFirst().orElse(null);

        // a mesma data de expiração vai para dentro do JWT e para a tabela token
        var dataExpiracao = getDataExpiracao();

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(subject)
                //converter instant
                .withExpiresAt(dataExpiracao.toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);

        tokenRepository.save(new Token(token, usuario, dataExpiracao));

        return token;
    }

    public UsuarioLogado verificadorToken(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algorithm).withIssuer(emissor).build();

        // confere a assinatura, o emissor e a validade do próprio JWT
        verificador.verify(token);

        // o token precisa existir no banco, estar ativo e dentro da validade
        var tokenBanco = tokenRepository.findByToken(token).orElse(null);

        if(tokenBanco == null || !tokenBanco.estaValido()){
            throw new IllegalArgumentException("Token inválido!");
        }

        return new UsuarioLogado(tokenBanco.getUsuario());
    }


    private LocalDateTime getDataExpiracao(){

        //pegar data atual
        var dataAtual = LocalDateTime.now();
        //adicionar o tempo contido na expiração e passa a contar a partir da data atual
        return dataAtual.plusMinutes(expiracao);
    }

}
