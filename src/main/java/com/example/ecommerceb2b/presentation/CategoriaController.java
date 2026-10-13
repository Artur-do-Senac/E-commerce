package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.services.CategoriaService;
import com.example.ecommerceb2b.domain.entities.Categoria;
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
    public ResponseEntity<List<Categoria>> listarTodos() {

        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {

        var categoriaEncontradaPorId = categoriaService.buscarCategoriaPorIdRetornaCategoriaOuNulo(id);

        // O Service retorna null quando o ID não existe, então a Controller responde 404
        if (categoriaEncontradaPorId != null) {
            return ResponseEntity.ok(categoriaEncontradaPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria uma nova categoria", description = "Cria um novo registro de categoria no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {

        var categoriaCriada = categoriaService.criarCategoriaRetornaCategoriaCriada(categoria);
        return ResponseEntity.ok(categoriaCriada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {

        var categoriaAtualizada = categoriaService.atualizarCategoriaRetornaCategoriaOuNulo(id, categoria);

        // null significa que não existe categoria com esse ID
        if (categoriaAtualizada != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {

        categoriaService.deletarCategoriaPorId(id);
        return ResponseEntity.ok().build();
    }
}
