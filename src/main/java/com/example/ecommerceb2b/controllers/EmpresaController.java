package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Empresa;
import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.repository.EmpresaRepository;
import com.example.ecommerceb2b.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {
    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    public ResponseEntity<List<Empresa>> listarTodos () {

        return ResponseEntity.ok(empresaRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa){
        var empresaBanco = empresaRepository.save(empresa);
        return ResponseEntity.ok(empresaBanco);
    }
}
