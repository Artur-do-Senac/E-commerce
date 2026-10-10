package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.*;
import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${spring.secret}")
    private String secret;

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

    // Busca um usuário pelo ID. Retorna o usuário encontrado ou null quando não existe (a Controller transforma o null em 404)
    public Usuario buscarUsuarioPorIdRetornaUsuarioOuNulo(Long id) {

        return usuarioRepository.findById(id).orElse(null);
    }

    // Salva um novo usuário no banco e retorna o usuário já salvo (com o ID gerado)
    public Usuario criarUsuarioRetornaUsuarioCriado(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }

    // Atualiza somente o status do usuário. Retorna o usuário atualizado ou null quando o ID não existe
    public Usuario atualizarStatusUsuarioRetornaUsuarioOuNulo(Long id, AttStatusRequest statusRequest) {

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco != null) {
            usuarioBanco.setStatus(statusRequest.statusUsuario());

            return usuarioRepository.save(usuarioBanco);
        }
        return null;
    }

    // Atualiza todos os dados do usuário. Retorna o usuário atualizado ou null quando o ID não existe
    public Usuario atualizarUsuarioRetornaUsuarioOuNulo(Long id, Usuario usuario) {

        try {
            Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

            if (usuarioBanco != null) {
                usuarioBanco.setStatus(usuario.getStatus());
                usuarioBanco.setNome(usuario.getNome());
                usuarioBanco.setEmail(usuario.getEmail());
                usuarioBanco.setCpf(usuario.getCpf());
                usuarioBanco.setSenha(usuario.getSenha());

                return usuarioRepository.save(usuarioBanco);
            }
            return null;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // Exclui o usuário pelo ID
    public void deletarUsuarioPorId(Long id) {

        usuarioRepository.deleteById(id);
    }

    public CriarAdminResponse criarAdmin(CriarAdminRequest criarAdminRequest) {

        if (!criarAdminRequest.secretKey().equals(secret)){
            return new CriarAdminResponse(0L, "Usuário salvo com sucesso!");
        }

        Usuario usuarioAdminSalvar = new Usuario(criarAdminRequest);
        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(usuarioAdminSalvar.getId(), "Usuário salvo com sucesso!");
    }
}







