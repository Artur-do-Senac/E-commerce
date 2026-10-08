package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.DTOs.AttStatusRequest;
import com.example.ecommerceb2b.application.DTOs.UsuarioResponse;
import com.example.ecommerceb2b.application.services.UsuarioService;
import com.example.ecommerceb2b.domain.entities.Usuario;
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

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {

        var usuarioEncontradoPorId = usuarioService.buscarUsuarioPorIdRetornaUsuarioOuNulo(id);

        // O Service retorna null quando o ID não existe, então a Controller responde 404
        if (usuarioEncontradoPorId != null) {
            return ResponseEntity.ok(usuarioEncontradoPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {

        var usuarioCriado = usuarioService.criarUsuarioRetornaUsuarioCriado(usuario);
        return ResponseEntity.ok(usuarioCriado);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de usuário no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusRequest statusRequest) {

        var usuarioComStatusAtualizado = usuarioService.atualizarStatusUsuarioRetornaUsuarioOuNulo(id, statusRequest);

        // null significa que não existe usuário com esse ID
        if (usuarioComStatusAtualizado != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {

        var usuarioAtualizado = usuarioService.atualizarUsuarioRetornaUsuarioOuNulo(id, usuario);

        // null significa que não existe usuário com esse ID
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {

        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.ok().build();
    }
}
