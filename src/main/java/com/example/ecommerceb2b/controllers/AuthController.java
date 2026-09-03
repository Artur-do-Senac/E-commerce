package com.example.ecommerceb2b.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ecommerceb2b.DTOs.EsqueciSenhaRequest;
import com.example.ecommerceb2b.DTOs.LoginRequest;
import com.example.ecommerceb2b.DTOs.LoginResponse;
import com.example.ecommerceb2b.DTOs.RedefinirSenhaRequest;
import com.example.ecommerceb2b.repository.UsuarioRepository;
import com.example.ecommerceb2b.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/auth")
@Tag(name= "Autenticação", description = "Controller de autenticação")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuários", description = "Método de login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request){

        if (usuarioRepository.existsUsuarioByEmailAndSenha(request.email(), request.senha())){

            var token = tokenService.gerarToken(request.email());

            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_UNAUTHORIZED).body("Usuário e/ou senha inválidos");
    }

    @PostMapping("/esqueci-senha")
    @Operation(summary = "Esqueci minha senha", description = "Gera um token de redefinição de senha para o e-mail informado")
    public ResponseEntity<?> esqueciSenha(@RequestBody EsqueciSenhaRequest senhaRequest) {

        var usuarioBanco = usuarioRepository.findByEmail(senhaRequest.email());

        if (usuarioBanco.isPresent()) {
            var token = tokenService.gerarToken(senhaRequest.email());

            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.status(HttpURLConnection.HTTP_NOT_FOUND).body("Usuário não encontrado");
    }

    @PostMapping("/redefinir-senha")
    @Operation(summary = "Redefinir senha", description = "Atualiza a senha do usuário a partir de um token de redefinição válido")
    public ResponseEntity<?> redefinirSenha(@RequestBody RedefinirSenhaRequest request) {

        try {
            var jwt = tokenService.verificadorToken(request.token());

            var usuarioBanco = usuarioRepository.findByEmail(jwt.getSubject());

            if (usuarioBanco.isPresent()) {
                var usuario = usuarioBanco.get();
                usuario.setSenha(request.novaSenha());
                usuarioRepository.save(usuario);

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpURLConnection.HTTP_NOT_FOUND).body("Usuário não encontrado");

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpURLConnection.HTTP_UNAUTHORIZED).body("Token inválido ou expirado");
        }
    }
}
