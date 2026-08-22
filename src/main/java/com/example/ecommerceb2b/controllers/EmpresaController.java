package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Empresa;
import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.repository.EmpresaRepository;
import com.example.ecommerceb2b.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(name= "Métodos de Empresas", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de empresas do sistema!")
public class EmpresaController {
    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping
    @Operation(summary = "Lista todas as empresas", description = "Retorna uma lista completa das empresas cadastradas no sistema.")
    public ResponseEntity<List<Empresa>> listarTodos () {

        return ResponseEntity.ok(empresaRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Lista todas as empresas", description = "Retorna uma lista completa das empresas cadastradas no sistema.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa){
        var empresaBanco = empresaRepository.save(empresa);
        return ResponseEntity.ok(empresaBanco);
    }
}
