package com.example.ecommerceb2b.infra.configuration;

import com.example.ecommerceb2b.domain.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Toda vez que o back sobe, as sessões anteriores deixam de valer: os tokens ativos viram inativos.
// Quem estava logado recebe 401 na próxima requisição e o front volta para a tela de login
@Component
public class InvalidarTokensAoIniciar implements ApplicationRunner {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void run(ApplicationArguments args) {
        tokenRepository.desativarTodos();
    }
}
