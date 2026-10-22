package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.DTOs.AttStatusProdutoRequest;
import com.example.ecommerceb2b.application.DTOs.ProdutoRequest;
import com.example.ecommerceb2b.application.DTOs.ProdutoResponse;
import com.example.ecommerceb2b.application.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
// Toda a regra de negócio e o acesso ao banco ficam no ProdutoService.
@RestController
@RequestMapping("/produtos")
@Tag(name= "Métodos de Produtos", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de produtos do sistema!")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;


    @GetMapping
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista completa dos produtos cadastrados no sistema.")
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {

        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {

        var produtoEncontradoPorId = produtoService.buscarPorId(id);

        // O Service retorna null quando o ID não existe, então a Controller responde 404
        if (produtoEncontradoPorId != null) {
            return ResponseEntity.ok(produtoEncontradoPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo produto", description = "Cria um novo registro de produto no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody ProdutoRequest produto) {

        try {
            ProdutoResponse produtoCriado = produtoService.salvarProduto(produto, 0L);
            return ResponseEntity.ok(produtoCriado);
        } catch (Exception e) {
            // Dado inválido (preço, estoque...): responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de produto no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusProdutoRequest statusRequest) {

        var produtoComStatusAtualizado = produtoService.atualizarStatus(statusRequest.statusProduto(), id);

        // false significa que não existe produto com esse ID
        if (produtoComStatusAtualizado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequest produto) {

        try {
            var produtoAtualizado = produtoService.salvarProduto(produto, id);

            // null significa que não existe produto com esse ID
            if (produtoAtualizado != null) {
                return ResponseEntity.ok(produtoAtualizado);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Dado inválido (preço, estoque...): responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {

        produtoService.deletarProdutoPorId(id);
        return ResponseEntity.ok().build();
    }
}
