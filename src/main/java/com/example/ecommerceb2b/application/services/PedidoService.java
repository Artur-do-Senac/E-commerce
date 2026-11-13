package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import com.example.ecommerceb2b.application.DTOs.ItemPedidoRequest;
import com.example.ecommerceb2b.application.DTOs.PedidoRequest;
import com.example.ecommerceb2b.application.DTOs.PedidoResponse;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.domain.entities.ItemPedido;
import com.example.ecommerceb2b.domain.entities.Pedido;
import com.example.ecommerceb2b.domain.repository.PedidoRepository;
import com.example.ecommerceb2b.domain.repository.ProdutoRepository;
import com.example.ecommerceb2b.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Camada de Application: concentra a lógica dos pedidos e conversa com o Repository (banco de dados).
// A PedidoController só chama os métodos daqui.
// Um pedido pertence a um cliente e pode ter itens de várias empresas: o GERENTE enxerga o pedido, mas só com os itens da empresa dele
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // O pedido tem pelo menos um item de produto da empresa?
    private boolean temItemDaEmpresa(Pedido pedido, Long empresaId) {

        return empresaId != null && pedido.getItens().stream().anyMatch(item -> item.pertenceA(empresaId));
    }

    // O ADMIN enxerga os pedidos completos. O GERENTE enxerga os pedidos que têm produtos da empresa dele, só com esses itens (e o valor calculado só com eles)
    public List<PedidoResponse> listarTodos() {

        var usuarioLogado = UsuarioLogado.atual();

        if (usuarioLogado.admin()) {
            return pedidoRepository.findAll()
                    .stream()
                    .map(PedidoResponse::new)
                    .toList();
        }

        if (usuarioLogado.empresa_id() == null) {
            return List.of();
        }

        return pedidoRepository.findDistinctByItens_Produto_Empresa_Id(usuarioLogado.empresa_id())
                .stream()
                .map(pedido -> new PedidoResponse(pedido, usuarioLogado.empresa_id()))
                .toList();
    }

    // Busca um pedido pelo ID. Retorna o PedidoResponse (nunca a entidade) ou null quando não existe ou não tem produtos da empresa de quem está logado (a Controller transforma o null em 404)
    public PedidoResponse buscarPorId(Long id) {

        var pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {
            return null;
        }

        var usuarioLogado = UsuarioLogado.atual();

        if (usuarioLogado.admin()) {
            return new PedidoResponse(pedido);
        }

        if (temItemDaEmpresa(pedido, usuarioLogado.empresa_id())) {
            return new PedidoResponse(pedido, usuarioLogado.empresa_id());
        }
        return null;
    }

    // Cria um pedido do usuário logado a partir dos itens (produto + quantidade). O valor total é calculado a partir dos preços atuais dos produtos
    // Dados inválidos (sem itens, produto inexistente, quantidade...) lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public PedidoResponse criarPedido(PedidoRequest pedido) {

        List<ItemPedido> itens = pedido.itens() == null ? List.of() : pedido.itens().stream()
                .map(this::criarItem)
                .toList();

        var cliente = usuarioRepository.findById(UsuarioLogado.atual().id()).orElse(null);

        // Para comprar o cliente precisa ter completado o cadastro com o endereço de entrega
        if (cliente == null || cliente.getEndereco() == null) {
            throw new IllegalArgumentException("Complete seu cadastro com o endereco para finalizar o pedido!");
        }

        Pedido pedidoBanco = new Pedido(cliente, itens);
        pedidoRepository.save(pedidoBanco);

        return new PedidoResponse(pedidoBanco);
    }

    private ItemPedido criarItem(ItemPedidoRequest item) {

        var produto = item.produtoId() == null ? null : produtoRepository.findById(item.produtoId()).orElse(null);

        return new ItemPedido(produto, item.quantidade());
    }

    // O GERENTE conclui os itens dos produtos da empresa dele (o estoque desses produtos é baixado). Retorna true quando concluiu e false quando o ID não existe ou o pedido não tem produtos da empresa dele
    // Sem item pendente ou sem estoque suficiente lança IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    @Transactional
    public boolean concluirItens(Long id) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        var empresaId = UsuarioLogado.atual().empresa_id();

        if (pedidoBanco == null || !temItemDaEmpresa(pedidoBanco, empresaId)) {
            return false;
        }

        pedidoBanco.concluirItensDaEmpresa(empresaId);

        pedidoBanco.getItens().stream()
                .filter(item -> item.pertenceA(empresaId))
                .forEach(item -> produtoRepository.save(item.getProduto()));
        pedidoRepository.save(pedidoBanco);

        return true;
    }

    // Atualiza somente o status do pedido. Retorna true quando atualizou e false quando o ID não existe ou não é de uma empresa de quem está logado
    public boolean atualizarStatus(EnumStatusPedido status, Long id) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null
                && (UsuarioLogado.atual().admin() || temItemDaEmpresa(pedidoBanco, UsuarioLogado.atual().empresa_id()))) {
            pedidoBanco.setStatus(status);
            pedidoRepository.save(pedidoBanco);

            return true;
        }
        return false;
    }

    // Exclui o pedido pelo ID (os itens do pedido são excluídos junto)
    public void deletarPedidoPorId(Long id) {

        pedidoRepository.deleteById(id);
    }
}
