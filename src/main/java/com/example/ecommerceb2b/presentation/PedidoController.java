package com.example.ecommerceb2b.presentation;

import com.example.ecommerceb2b.application.DTOs.AttStatusPedidoRequest;
import com.example.ecommerceb2b.application.DTOs.PedidoRequest;
import com.example.ecommerceb2b.application.DTOs.PedidoResponse;
import com.example.ecommerceb2b.application.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Camada de Presentation: a Controller só recebe a requisição HTTP, chama o Service e devolve a resposta (status + corpo).
// Toda a regra de negócio e o acesso ao banco ficam no PedidoService.
@RestController
@RequestMapping("/pedidos")
@Tag(name= "Métodos de Pedidos", description = "Grupo de API's responsável por controlar a estrutura de criação e consulta de pedidos do sistema!")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;


    @GetMapping
    @Operation(summary = "Lista os pedidos", description = "O ADMIN recebe todos os pedidos. O GERENTE recebe os pedidos que têm produtos da empresa dele, somente com esses itens.")
    public ResponseEntity<List<PedidoResponse>> listarTodos() {

        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista o registro por ID", description = "Lista o registro por ID no sistema")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {

        var pedidoEncontradoPorId = pedidoService.buscarPorId(id);

        // O Service retorna null quando o ID não existe ou não tem produtos da empresa, então a Controller responde 404
        if (pedidoEncontradoPorId != null) {
            return ResponseEntity.ok(pedidoEncontradoPorId);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Cria um pedido do usuário logado a partir dos itens (produto + quantidade). O valor total é calculado pelo sistema.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> criar(@RequestBody PedidoRequest pedido) {

        try {
            PedidoResponse pedidoCriado = pedidoService.criarPedido(pedido);
            return ResponseEntity.ok(pedidoCriado);
        } catch (Exception e) {
            // Dado inválido (sem itens, produto, quantidade...): responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Conclui os itens da empresa", description = "O GERENTE conclui os itens dos produtos da empresa dele no pedido e o estoque desses produtos é baixado. O pedido fica concluído quando todas as empresas concluem os seus itens.")
    public ResponseEntity<?> concluirItens(@PathVariable Long id) {

        try {
            var itensConcluidos = pedidoService.concluirItens(id);

            // false significa que não existe pedido com esse ID ou que ele não tem produtos da empresa
            if (itensConcluidos) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Sem item pendente ou sem estoque suficiente: responde 400 com a mensagem do erro
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status", description = "Atualiza status de pedido no sistema")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AttStatusPedidoRequest statusRequest) {

        var pedidoComStatusAtualizado = pedidoService.atualizarStatus(statusRequest.statusPedido(), id);

        // false significa que não existe pedido com esse ID
        if (pedidoComStatusAtualizado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/excluir")
    @Operation(summary = "Deleta o registro por ID", description = "Deleta o registro por ID no sistema")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {

        pedidoService.deletarPedidoPorId(id);
        return ResponseEntity.ok().build();
    }
}
