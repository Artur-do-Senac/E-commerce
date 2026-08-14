package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping
    public ResponseEntity<?> listarTodos () {
        List<Usuario> usuarios = List.of(new Usuario(1L, "Samuel", "06372005948", "123456", "samuel@gmail.com"));

        return ResponseEntity.ok(usuarios);

    }

}
