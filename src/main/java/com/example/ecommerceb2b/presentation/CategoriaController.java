package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.DTOs.CategoriaRequest;
import com.example.ecommerceb2b.application.DTOs.CategoriaResponse;
import com.example.ecommerceb2b.application.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
// Toda a regra de negócio e o acesso ao banco ficam no CategoriaService.
@RestController
@RequestMapping("/categorias")
@Tag(name= "Métodos de Categorias", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de categorias do sistema!")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Lista todas as categorias", description = "Retorna uma lista completa das categorias cadastradas no sistema.")
    public ResponseEntity<List<CategoriaResponse>> listarTodos() {

        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {

        var categoriaEncontradaPorId = categoriaService.buscarPorId(id);

        // O Service retorna null quando o ID não existe, então a Controller responde 404
        if (categoriaEncontradaPorId != null) {
            return ResponseEntity.ok(categoriaEncontradaPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria uma nova categoria", description = "Cria um novo registro de categoria no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody CategoriaRequest categoria) {

        try {
            CategoriaResponse categoriaCriada = categoriaService.salvarCategoria(categoria, 0L);
            return ResponseEntity.ok(categoriaCriada);
        } catch (Exception e) {
            // Dado inválido: responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<?> atualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequest categoria) {

        try {
            var categoriaAtualizada = categoriaService.salvarCategoria(categoria, id);

            // null significa que não existe categoria com esse ID
            if (categoriaAtualizada != null) {
                return ResponseEntity.ok(categoriaAtualizada);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Dado inválido: responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {

        categoriaService.deletarCategoriaPorId(id);
        return ResponseEntity.ok().build();
    }
}
