package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.DTOs.AttStatusEmpresaRequest;
import com.example.ecommerceb2b.application.services.EmpresaService;
import com.example.ecommerceb2b.domain.entities.Empresa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
// Toda a regra de negócio e o acesso ao banco ficam no EmpresaService.
@RestController
@RequestMapping("/empresas")
@Tag(name= "Métodos de Empresas", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de empresas do sistema!")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;


    @GetMapping
    @Operation(summary = "Lista todas as empresas", description = "Retorna uma lista completa das empresas cadastrados no sistema.")
    public ResponseEntity<List<Empresa>> listarTodos() {

        return ResponseEntity.ok(empresaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {

        var empresaEncontradaPorId = empresaService.buscarEmpresaPorIdRetornaEmpresaOuNulo(id);

        // O Service retorna null quando o ID não existe, então a Controller responde 404
        if (empresaEncontradaPorId != null) {
            return ResponseEntity.ok(empresaEncontradaPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria uma nova empresa", description = "Cria um novo registro de empresa no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {

        var empresaCriada = empresaService.criarEmpresaRetornaEmpresaCriada(empresa);
        return ResponseEntity.ok(empresaCriada);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de empresa no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusEmpresaRequest statusRequest) {

        var empresaComStatusAtualizado = empresaService.atualizarStatusEmpresaRetornaEmpresaOuNulo(id, statusRequest);

        // null significa que não existe empresa com esse ID
        if (empresaComStatusAtualizado != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<Empresa> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {

        var empresaAtualizada = empresaService.atualizarEmpresaRetornaEmpresaOuNulo(id, empresa);

        // null significa que não existe empresa com esse ID
        if (empresaAtualizada != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {

        empresaService.deletarEmpresaPorId(id);
        return ResponseEntity.ok().build();
    }
}
