package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.AtualizarStatusRequest;
import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name= "Métodos de Usuários", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de usuários do sistema!")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;


    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa dos usuários cadastrados no sistema.")
    public ResponseEntity<List<Pedido>> listarTodos() {

        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            return ResponseEntity.ok(pedidoBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        var pedidoBanco = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoBanco);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            pedidoBanco.setStatus(statusRequest.status());
            pedidoRepository.save(pedidoBanco);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {

        try {
            Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

            if (pedidoBanco != null) {
                pedidoBanco.setStatus(pedido.getStatus());
                pedidoBanco.setValorTotal(pedido.getValorTotal());
                pedidoBanco.setData(pedido.getData());
                pedidoBanco.setDesconto(pedido.getDesconto());
                pedidoRepository.save(pedidoBanco);

                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}