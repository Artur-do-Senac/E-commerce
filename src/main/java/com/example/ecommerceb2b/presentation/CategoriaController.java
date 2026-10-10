package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.services.CategoriaService;
import com.example.ecommerceb2b.domain.entities.Categoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
// Toda a regra de negócio e o acesso ao banco ficam no CategoriaService.
@RestController
@RequestMapping("/categorias")
@Tag(name= "Métodos de Categorias", description = "Grupo de API's responsável por controlar a estrutura de criação de categorias do sistema!")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Cria uma nova categoria", description = "Cria um novo registro de categoria no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {

        var categoriaCriada = categoriaService.criarCategoriaRetornaCategoriaCriada(categoria);
        return ResponseEntity.ok(categoriaCriada);
    }
}
