package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.*;
import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.repository.UsuarioRepository;
import com.example.ecommerceb2b.domain.valueobject.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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

        // Usuário excluído (status EXCLUIDO) não consegue mais entrar no sistema
        if (usuarioRepository.existsUsuarioByEmailAndSenhaAndStatusNot(request.email(), request.senha(), EnumStatusUsuario.EXCLUIDO)) {

            var token = tokenService.gerarToken(request.email());

            return new LoginResponse((token));
        }
        return null;
    }

    // O usuário logado é o principal que o JwtFilter colocou no contexto de segurança
    private UsuarioLogado getUsuarioLogado() {
        return (UsuarioLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Retorna os usuários da empresa de quem está logado, menos os excluídos (exclusão lógica: status EXCLUIDO)
    // Quem não pertence a nenhuma empresa (ex.: o ADMIN) enxerga os usuários de todas
    public List<UsuarioResponse> listarTodos() {

        var usuarioLogado = getUsuarioLogado();

        List<Usuario> usuarios = usuarioLogado.empresa_id() == null
                ? usuarioRepository.findByStatusNot(EnumStatusUsuario.EXCLUIDO).orElse(List.of())
                : usuarioRepository.getUsuariosByEmpresa_id(usuarioLogado.empresa_id());

        return usuarios.stream()
                .filter(usuario -> usuario.getStatus() != EnumStatusUsuario.EXCLUIDO)
                .map(UsuarioResponse::new)
                .toList();
    }

    // Busca um usuário pelo ID. Retorna o UsuarioResponse (nunca a entidade) ou null quando não existe (a Controller transforma o null em 404)
    public UsuarioResponse buscarPorId(Long id) {

        var usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
            return null;
        }
        return new UsuarioResponse(usuario);
    }

    // Salva o usuário: com id 0 cria um novo, com qualquer outro id atualiza o existente. Retorna o usuário salvo ou null quando o ID não existe
    // CPF ou e-mail inválido lança IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public UsuarioResponse salvarUsuario(UsuarioRequest usuario, Long id) {

        // Novo usuário
        if (id == 0L) {
            Usuario usuarioBanco = new Usuario(usuario);
            usuarioRepository.save(usuarioBanco);

            return new UsuarioResponse(usuarioBanco);
        }

        // Usuário existente
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco != null) {
            usuarioBanco.setNome(usuario.nome());
            usuarioBanco.setCpf(new CPF(usuario.cpf()));
            usuarioBanco.informarTelefone(usuario.telefone());
            usuarioBanco.setEmail(usuario.email());
            usuarioBanco.setSenha(usuario.senha());
            usuarioRepository.save(usuarioBanco);

            return new UsuarioResponse(usuarioBanco);
        }
        return null;
    }

    // Atualiza somente o status do usuário. Retorna true quando atualizou e false quando o ID não existe
    // A exclusão também passa por aqui: excluir é trocar o status para EXCLUIDO (o registro continua no banco)
    public boolean atualizarStatus(EnumStatusUsuario status, Long id) {

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco != null) {
            usuarioBanco.setStatus(status);
            usuarioRepository.save(usuarioBanco);

            return true;
        }
        return false;
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







