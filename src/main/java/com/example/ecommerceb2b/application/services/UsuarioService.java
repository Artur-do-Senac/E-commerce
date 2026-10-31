package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.*;
import com.example.ecommerceb2b.domain.entities.Empresa;
import com.example.ecommerceb2b.domain.entities.Usuario;
import com.example.ecommerceb2b.domain.repository.EmpresaRepository;
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
    private EmpresaRepository empresaRepository;

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
    // O ADMIN (global) e quem não pertence a nenhuma empresa enxergam os usuários de todas
    public List<UsuarioResponse> listarTodos() {

        var usuarioLogado = getUsuarioLogado();

        List<Usuario> usuarios = usuarioLogado.admin() || usuarioLogado.empresa_id() == null
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

        // Empresa do usuário. Se não existir, fica null e a entidade recusa com "Empresa Invalida!" (exceto para ADMIN)
        Empresa empresa = usuario.empresaId() == null ? null : empresaRepository.findById(usuario.empresaId()).orElse(null);

        // Novo usuário
        if (id == 0L) {
            Usuario usuarioBanco = new Usuario(usuario, empresa);
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
            usuarioBanco.informarPerfil(usuario.perfil());
            usuarioBanco.informarEmpresa(empresa);
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

    // Retorna os dados do usuário dono do token (o principal que o JwtFilter colocou no contexto de segurança)
    public UsuarioResponse buscarUsuarioLogado() {
        try {
            var usuarioLogado = getUsuarioLogado();

            return usuarioRepository.findById(usuarioLogado.id())
                    .map(UsuarioResponse::new)
                    .orElse(null);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário logado!");
        }
    }

    // Cold start: cria o primeiro ADMIN (chamado pelo app desktop, sem token)
    // Só funciona com a chave secreta correta e enquanto não existir nenhum ADMIN; senão lança IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public CriarAdminResponse criarAdmin(CriarAdminRequest criarAdminRequest) {

        if (criarAdminRequest.secretKey() == null || !criarAdminRequest.secretKey().equals(secret)){
            throw new IllegalArgumentException("Chave secreta invalida!");
        }

        if (usuarioRepository.existsByRole("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Ja existe um administrador cadastrado!");
        }

        Usuario usuarioAdminSalvar = new Usuario(criarAdminRequest);
        usuarioRepository.save(usuarioAdminSalvar);

        return new CriarAdminResponse(usuarioAdminSalvar.getId(), "Usuário salvo com sucesso!");
    }
}







