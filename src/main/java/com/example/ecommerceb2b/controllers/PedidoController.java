package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.entities.Usuario;
import com.example.ecommerceb2b.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name= "Métodos de Pedidos", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de pedidos do sistema!")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    @Operation(summary = "Lista todos os pedidos", description = "Retorna uma lista completa dos pedidos cadastrados no sistema.")
    public ResponseEntity<List<Pedido>> listarTodos () {

        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Cria um novo registro de pedido no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido){
        var pedidoBanco = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoBanco);
    }
}
