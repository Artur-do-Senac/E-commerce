package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.LoginRequest;
import com.example.ecommerceb2b.application.DTOs.LoginResponse;
import com.example.ecommerceb2b.application.DTOs.UsuarioResponse;
import com.example.ecommerceb2b.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    public LoginResponse validarUsuarioAutenticadoRetornaToken (LoginRequest request){

        if (usuarioRepository.existsUsuarioByEmailAndSenha(request.email(), request.senha())) {

            var token = tokenService.gerarToken(request.email());

            return new LoginResponse((token));
        }
        return null;
    }

    public List<UsuarioResponse> listarTodos() {

        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::new)
                .toList();
    }
}
