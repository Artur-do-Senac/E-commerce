package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.entities.Produto;
import com.example.ecommerceb2b.repository.PedidoRepository;
import com.example.ecommerceb2b.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos(){

        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> criar(@RequestBody Produto produto){
        var produtoBanco = produtoRepository.save(produto);
        return ResponseEntity.ok(produtoBanco);
    }
}
