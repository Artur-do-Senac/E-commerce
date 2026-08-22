package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.entities.Produto;
import com.example.ecommerceb2b.repository.PedidoRepository;
import com.example.ecommerceb2b.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name= "Métodos de Produtos", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de produtos do sistema!")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista completa dos produtos cadastrados no sistema.")
    public ResponseEntity<List<Produto>> listarTodos(){

        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Cria um novo produto", description = "Cria um novo registro de produto no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> criar(@RequestBody Produto produto){
        var produtoBanco = produtoRepository.save(produto);
        return ResponseEntity.ok(produtoBanco);
    }
}
