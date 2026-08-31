package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.AtualizarStatusRequest;
import com.example.ecommerceb2b.entities.Produto;
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
@Tag(name= "Métodos de Usuários", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de usuários do sistema!")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;


    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa dos usuários cadastrados no sistema.")
    public ResponseEntity<List<Produto>> listarTodos() {

        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null) {
            return ResponseEntity.ok(produtoBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        var produtoBanco = produtoRepository.save(produto);
        return ResponseEntity.ok(produtoBanco);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest) {

        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null) {
            produtoBanco.setStatus(statusRequest.status());
            produtoRepository.save(produtoBanco);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {

        try {
            Produto produtoBanco = produtoRepository.findById(id).orElse(null);

            if (produtoBanco != null) {
                produtoBanco.setStatus(produto.getStatus());
                produtoBanco.setNome(produto.getNome());
                produtoBanco.setPreco(produto.getPreco());
                produtoBanco.setVencimento(produto.getVencimento());
                produtoRepository.save(produtoBanco);

                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
