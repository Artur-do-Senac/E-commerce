package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.DTOs.*;
import com.example.ecommerceb2b.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
// Toda a regra de negócio e o acesso ao banco ficam no UsuarioService.
@RestController
@RequestMapping("/usuarios")
@Tag(name= "Métodos de Usuários", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de usuários do sistema!")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa dos usuários cadastrados no sistema.")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {

        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping("/admin")
    public ResponseEntity<CriarAdminResponse> criarAdmin (@RequestBody CriarAdminRequest criarAdminRequest) {

        try {
            CriarAdminResponse respostaSalvar = usuarioService.criarAdmin(criarAdminRequest);
            return ResponseEntity.ok(respostaSalvar);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }

    }


    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {

        var usuarioEncontradoPorId = usuarioService.buscarPorId(id);

        // O Service retorna null quando o ID não existe, então a Controller responde 404
        if (usuarioEncontradoPorId != null) {
            return ResponseEntity.ok(usuarioEncontradoPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody UsuarioRequest usuario) {

        try {
            UsuarioResponse usuarioCriado = usuarioService.salvarUsuario(usuario, 0L);
            return ResponseEntity.ok(usuarioCriado);
        } catch (Exception e) {
            // CPF ou e-mail inválido: responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de usuário no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusRequest statusRequest) {

        var usuarioComStatusAtualizado = usuarioService.atualizarStatus(statusRequest.statusUsuario(), id);

        // false significa que não existe usuário com esse ID
        if (usuarioComStatusAtualizado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest usuario) {

        try {
            var usuarioAtualizado = usuarioService.salvarUsuario(usuario, id);

            // null significa que não existe usuário com esse ID
            if (usuarioAtualizado != null) {
                return ResponseEntity.ok(usuarioAtualizado);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // CPF ou e-mail inválido: responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Exclui o registro por ID", description = "Exclui o registro por ID no sistema (exclusão lógica: o status passa para EXCLUIDO e o registro continua no banco)")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {

        var usuarioExcluido = usuarioService.atualizarStatus(EnumStatusUsuario.EXCLUIDO, id);

        // false significa que não existe usuário com esse ID
        if (usuarioExcluido) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
