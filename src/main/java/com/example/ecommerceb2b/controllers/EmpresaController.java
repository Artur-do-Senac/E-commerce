package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.AttStatusEmpresaRequest;
import com.example.ecommerceb2b.entities.Empresa;
import com.example.ecommerceb2b.repository.EmpresaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(name= "Métodos de Empresas", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de empresas do sistema!")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;


    @GetMapping
    @Operation(summary = "Lista todas as empresas", description = "Retorna uma lista completa das empresas cadastrados no sistema.")
    public ResponseEntity<List<Empresa>> listarTodos() {

        return ResponseEntity.ok(empresaRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            return ResponseEntity.ok(empresaBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria uma nova empresa", description = "Cria um novo registro de empresa no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        var empresaBanco = empresaRepository.save(empresa);
        return ResponseEntity.ok(empresaBanco);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de empresa no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusEmpresaRequest statusRequest) {

        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            empresaBanco.setStatus(statusRequest.statusEmpresa());
            empresaRepository.save(empresaBanco);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o registro por ID", description = "Atualiza o registro por ID no sistema")
    public ResponseEntity<Empresa> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {

        try {
            Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

            if (empresaBanco != null) {
                empresaBanco.setStatus(empresa.getStatus());
                empresaBanco.setRazaoSocial(empresa.getRazaoSocial());
                empresaBanco.setCnpj(empresa.getCnpj());
                empresaRepository.save(empresaBanco);

                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        empresaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
