package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.AttStatusPedidoRequest;
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
    public List<Pedido> listarTodos() {

        return pedidoRepository.findAll();
    }

    // Busca um pedido pelo ID. Retorna o pedido encontrado ou null quando não existe (a Controller transforma o null em 404)
    public Pedido buscarPedidoPorIdRetornaPedidoOuNulo(Long id) {

        return pedidoRepository.findById(id).orElse(null);
    }

    // Salva um novo pedido no banco e retorna o pedido já salvo (com o ID gerado)
    public Pedido criarPedidoRetornaPedidoCriado(Pedido pedido) {

        return pedidoRepository.save(pedido);
    }

    // Atualiza somente o status do pedido. Retorna o pedido atualizado ou null quando o ID não existe
    public Pedido atualizarStatusPedidoRetornaPedidoOuNulo(Long id, AttStatusPedidoRequest statusRequest) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null) {
            pedidoBanco.setStatus(statusRequest.statusPedido());

            return pedidoRepository.save(pedidoBanco);
        }
        return null;
    }

    // Atualiza todos os dados do pedido. Retorna o pedido atualizado ou null quando o ID não existe
    public Pedido atualizarPedidoRetornaPedidoOuNulo(Long id, Pedido pedido) {

        try {
            Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

            if (pedidoBanco != null) {
                pedidoBanco.setStatus(pedido.getStatus());
                pedidoBanco.setValorTotal(pedido.getValorTotal());
                pedidoBanco.setData(pedido.getData());
                pedidoBanco.setDesconto(pedido.getDesconto());

                return pedidoRepository.save(pedidoBanco);
            }
            return null;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // Exclui o pedido pelo ID
    public void deletarPedidoPorId(Long id) {

        pedidoRepository.deleteById(id);
    }
}
