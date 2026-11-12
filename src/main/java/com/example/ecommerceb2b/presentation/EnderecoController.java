package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.services.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
@RestController
@RequestMapping("/endereco")
@Tag(name= "Métodos de Endereço", description = "Grupo de API's responsável por consultar endereços em serviço externo (ViaCEP)!")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("/{cep}")
    @Operation(summary = "Busca o endereço pelo CEP", description = "Consulta o CEP na API externa do ViaCEP e retorna o endereço")
    public ResponseEntity<?> buscarEndereco(@PathVariable String cep) {

        try {
            return ResponseEntity.ok(enderecoService.buscarEndereco(cep));
        } catch (Exception e) {
            // CEP inexistente ou falha na consulta externa: responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
