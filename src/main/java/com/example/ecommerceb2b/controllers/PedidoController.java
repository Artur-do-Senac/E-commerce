package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.AttStatusPedidoRequest;
import com.example.ecommerceb2b.DTOs.AttStatusRequest;
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
@Tag(name= "Métodos de Pedidos", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de pedidos do sistema!")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;


    @GetMapping
    @Operation(summary = "Lista todos os pedidos", description = "Retorna uma lista completa dos pedidos cadastrados no sistema.")
    public ResponseEntity<List<Pedido>> listarTodos() {

        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            return ResponseEntity.ok(pedidoBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Cria um novo registro de pedido no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        var pedidoBanco = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoBanco);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de pedido no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusPedidoRequest statusRequest) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            pedidoBanco.setStatus(statusRequest.statusPedido());
            pedidoRepository.save(pedidoBanco);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
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
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}