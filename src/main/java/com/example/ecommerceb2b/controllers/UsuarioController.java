package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Usuario;
import com.example.ecommerceb2b.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa dos usuários cadastrados no sistema.")
    public ResponseEntity<List<Usuario>> listarTodos () {

        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario){
        var usuarioBanco = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioBanco);
    }

}
