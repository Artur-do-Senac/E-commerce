package com.example.ecommerceb2b.presentation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ecommerceb2b.application.DTOs.EsqueciSenhaRequest;
import com.example.ecommerceb2b.application.DTOs.LoginRequest;
import com.example.ecommerceb2b.application.DTOs.LoginResponse;
import com.example.ecommerceb2b.application.DTOs.RedefinirSenhaRequest;
import com.example.ecommerceb2b.application.services.TokenService;
import com.example.ecommerceb2b.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;

@RestController
@RequestMapping("/auth")
@Tag(name= "Autenticação", description = "Controller de autenticação")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    @Operation(summary = "Autenticação de usuários", description = "Método de login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request){

        var resultadoAutenticacaoRetornoToken = usuarioService.validarUsuarioAutenticadoRetornaToken(request);

        if (resultadoAutenticacaoRetornoToken != null){

            return ResponseEntity.ok(resultadoAutenticacaoRetornoToken);
        }
        return ResponseEntity.badRequest().body("Usuário e/ou senha inválidos");
    }

    // O JwtFilter já validou o token antes de chegar aqui, então o cabeçalho Authorization sempre vem no formato "Bearer <token>"
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalida o token enviado no cabeçalho Authorization: ele deixa de ser aceito nas próximas requisições")
    public ResponseEntity<Void> logout (@RequestHeader("Authorization") String authorization){

        tokenService.invalidarToken(authorization.substring("Bearer ".length()));

        return ResponseEntity.ok().build();
    }

    /* PARA AJUSTAR O RESTANTE, É NECESSÁRIO CRIAR UM METODO PRA CADA UM DENTRO DE USUARIOSERVICE */

/*
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
    }*/
}
