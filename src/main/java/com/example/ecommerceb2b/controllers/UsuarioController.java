package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.AttStatusRequest;
import com.example.ecommerceb2b.entities.Usuario;
import com.example.ecommerceb2b.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name= "Métodos de Usuários", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de usuários do sistema!")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa dos usuários cadastrados no sistema.")
    public ResponseEntity<List<Usuario>> listarTodos() {

        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco != null) {
            return ResponseEntity.ok(usuarioBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        var usuarioBanco = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioBanco);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de usuário no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusRequest statusRequest) {

        Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

        if (usuarioBanco != null) {
            usuarioBanco.setStatus(statusRequest.statusUsuario());
            usuarioRepository.save(usuarioBanco);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {

        try {
            Usuario usuarioBanco = usuarioRepository.findById(id).orElse(null);

            if (usuarioBanco != null) {
                usuarioBanco.setStatus(usuario.getStatus());
                usuarioBanco.setNome(usuario.getNome());
                usuarioBanco.setEmail(usuario.getEmail());
                usuarioBanco.setCpf(usuario.getCpf());
                usuarioBanco.setSenha(usuario.getSenha());
                usuarioRepository.save(usuarioBanco);

                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}