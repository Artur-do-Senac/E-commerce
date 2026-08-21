package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.entities.Usuario;
import com.example.ecommerceb2b.repository.PedidoRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos () {

        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido){
        var pedidoBanco = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoBanco);
    }
}
