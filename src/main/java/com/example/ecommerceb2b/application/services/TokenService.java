package com.example.ecommerceb2b.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.domain.entities.Token;
import com.example.ecommerceb2b.domain.repository.TokenRepository;
import com.example.ecommerceb2b.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.time.Instant;
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
        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);

            var usuario = usuarioRepository.findAll().stream().filter(u -> u.getEmail().equals(subject)).findFirst().orElse(null);

            String token = com.auth0.jwt
                    .JWT.create()
                    .withIssuer(emissor)
                    .withSubject(subject)
                    .withExpiresAt(getDataExpiracao())
                    .sign(algorithm);

            tokenRepository.save(new Token(token, usuario));

            return token;

        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public UsuarioLogado verificadorToken(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algorithm).withIssuer(emissor).build();

        var tokenBanco = tokenRepository.findByToken(token).orElse(null);

        if(tokenBanco == null){
            throw new IllegalArgumentException("Token inválido!");
        }

        return new UsuarioLogado(tokenBanco.getUsuario());
    }


    private Instant getDataExpiracao(){

        //pegar data atual
        var dataAtual = LocalDateTime.now();
        //adicionar o tempo contido na expiração e passa a contar a partir da data atual
        var dataFutura = dataAtual.plusMinutes(expiracao);

        //converter instant
        return dataFutura.toInstant(ZoneOffset.of("-03:00"));
    }

}
