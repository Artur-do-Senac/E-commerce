package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import com.example.ecommerceb2b.application.DTOs.PedidoRequest;
import com.example.ecommerceb2b.application.DTOs.PedidoResponse;
import com.example.ecommerceb2b.domain.entities.Pedido;
import com.example.ecommerceb2b.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Camada de Application: concentra a lógica dos pedidos e conversa com o Repository (banco de dados).
// A PedidoController só chama os métodos daqui.
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Retorna todos os pedidos cadastrados
    public List<PedidoResponse> listarTodos() {

        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponse::new)
                .toList();
    }

    // Busca um pedido pelo ID. Retorna o PedidoResponse (nunca a entidade) ou null quando não existe (a Controller transforma o null em 404)
    public PedidoResponse buscarPorId(Long id) {

        var pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {
            return null;
        }
        return new PedidoResponse(pedido);
    }

    // Salva o pedido: com id 0 cria um novo, com qualquer outro id atualiza o existente. Retorna o pedido salvo ou null quando o ID não existe
    // Dados inválidos (valor total, desconto...) lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public PedidoResponse salvarPedido(PedidoRequest pedido, Long id) {

        // Novo pedido
        if (id == 0L) {
            Pedido pedidoBanco = new Pedido(pedido);
            pedidoRepository.save(pedidoBanco);

            return new PedidoResponse(pedidoBanco);
        }

        // Pedido existente (o valor total vem antes do desconto, porque o desconto não pode passar do total)
        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            pedidoBanco.setValorTotal(pedido.valorTotal());
            pedidoBanco.setDesconto(pedido.desconto());
            pedidoBanco.setData(pedido.data());
            pedidoRepository.save(pedidoBanco);

            return new PedidoResponse(pedidoBanco);
        }
        return null;
    }

    // Atualiza somente o status do pedido. Retorna true quando atualizou e false quando o ID não existe
    public boolean atualizarStatus(EnumStatusPedido status, Long id) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            pedidoBanco.setStatus(status);
            pedidoRepository.save(pedidoBanco);

            return true;
        }
        return false;
    }

    // Exclui o pedido pelo ID
    public void deletarPedidoPorId(Long id) {

        pedidoRepository.deleteById(id);
    }
}
