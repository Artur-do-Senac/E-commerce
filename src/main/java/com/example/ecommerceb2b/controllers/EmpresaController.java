package com.example.ecommerceb2b.controllers;

import com.example.ecommerceb2b.DTOs.AtualizarStatusRequest;
import com.example.ecommerceb2b.entities.Empresa;
import com.example.ecommerceb2b.entities.Pedido;
import com.example.ecommerceb2b.entities.Empresa;
import com.example.ecommerceb2b.repository.EmpresaRepository;
import com.example.ecommerceb2b.repository.PedidoRepository;
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
@Tag(name= "Métodos de Usuários", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de usuários do sistema!")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;


    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista completa dos usuários cadastrados no sistema.")
    public ResponseEntity<List<Empresa>> listarTodos() {

        return ResponseEntity.ok(empresaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            return ResponseEntity.ok(empresaBanco);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo registro de usuário no sistema")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        var empresaBanco = empresaRepository.save(empresa);
        return ResponseEntity.ok(empresaBanco);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusRequest statusRequest) {

        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            empresaBanco.setStatus(statusRequest.status());
            empresaRepository.save(empresaBanco);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        empresaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
