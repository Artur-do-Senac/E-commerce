package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.AdicionarProdutoRequest;
import com.example.ecommerceb2b.application.DTOs.EnumStatusPedido;
import com.example.ecommerceb2b.application.DTOs.PedidoRequest;
import com.example.ecommerceb2b.application.DTOs.PedidoResponse;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.domain.entities.Empresa;
import com.example.ecommerceb2b.domain.entities.Pedido;
import com.example.ecommerceb2b.domain.entities.Produto;
import com.example.ecommerceb2b.domain.repository.EmpresaRepository;
import com.example.ecommerceb2b.domain.repository.PedidoRepository;
import com.example.ecommerceb2b.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Camada de Application: concentra a lógica dos pedidos e conversa com o Repository (banco de dados).
// A PedidoController só chama os métodos daqui.
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    // A empresa do usuário logado (null para quem não pertence a nenhuma, como o ADMIN)
    private Empresa getEmpresaUsuarioLogado() {

        var empresaId = UsuarioLogado.atual().empresa_id();

        if (empresaId == null) {
            return null;
        }
        return empresaRepository.findById(empresaId).orElse(null);
    }

    // Retorna os pedidos da empresa de quem está logado. O ADMIN enxerga os de todas as empresas
    public List<PedidoResponse> listarTodos() {

        var usuarioLogado = UsuarioLogado.atual();

        List<Pedido> pedidos;

        if (usuarioLogado.admin()) {
            pedidos = pedidoRepository.findAll();
        } else if (usuarioLogado.empresa_id() == null) {
            pedidos = List.of();
        } else {
            pedidos = pedidoRepository.getPedidosByEmpresa_Id(usuarioLogado.empresa_id());
        }

        return pedidos.stream()
                .map(PedidoResponse::new)
                .toList();
    }

    // Busca um pedido pelo ID. Retorna o PedidoResponse (nunca a entidade) ou null quando não existe ou é de outra empresa (a Controller transforma o null em 404)
    public PedidoResponse buscarPorId(Long id) {

        var pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null || !UsuarioLogado.atual().podeAcessar(pedido.getEmpresa())) {
            return null;
        }
        return new PedidoResponse(pedido);
    }

    // Salva o pedido: com id 0 cria um novo (da empresa de quem está logado), com qualquer outro id atualiza o existente. Retorna o pedido salvo ou null quando o ID não existe ou é de outra empresa
    // Dados inválidos (valor total, desconto...) lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public PedidoResponse salvarPedido(PedidoRequest pedido, Long id) {

        // Novo pedido
        if (id == 0L) {
            Pedido pedidoBanco = new Pedido(pedido);
            pedidoBanco.setEmpresa(getEmpresaUsuarioLogado());
            pedidoRepository.save(pedidoBanco);

            return new PedidoResponse(pedidoBanco);
        }

        // Pedido existente (o valor total vem antes do desconto, porque o desconto não pode passar do total)
        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null && UsuarioLogado.atual().podeAcessar(pedidoBanco.getEmpresa())) {
            pedidoBanco.setValorTotal(pedido.valorTotal());
            pedidoBanco.setDesconto(pedido.desconto());
            pedidoBanco.setData(pedido.data());
            pedidoRepository.save(pedidoBanco);

            return new PedidoResponse(pedidoBanco);
        }
        return null;
    }

    // Atualiza somente o status do pedido. Retorna true quando atualizou e false quando o ID não existe ou é de outra empresa
    public boolean atualizarStatus(EnumStatusPedido status, Long id) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);

        if (pedidoBanco != null && UsuarioLogado.atual().podeAcessar(pedidoBanco.getEmpresa())) {
            pedidoBanco.setStatus(status);
            pedidoRepository.save(pedidoBanco);

            return true;
        }
        return false;
    }

    // Adiciona um produto ao pedido. Retorna true quando adicionou (ou o produto já estava no pedido) e false quando o pedido ou o produto não existem ou são de outra empresa
    public boolean adicionarProduto(Long id, AdicionarProdutoRequest adicionarProdutoRequest) {

        Pedido pedidoBanco = pedidoRepository.findById(id).orElse(null);
        Produto produtoBanco = produtoRepository.findById(adicionarProdutoRequest.produto_id()).orElse(null);

        if (pedidoBanco != null && produtoBanco != null
                && UsuarioLogado.atual().podeAcessar(pedidoBanco.getEmpresa())
                && UsuarioLogado.atual().podeAcessar(produtoBanco.getEmpresa())) {

            var produtoJaAdicionado = pedidoBanco.getProdutos().stream()
                    .anyMatch(p -> p.getId().equals(produtoBanco.getId()));

            if (!produtoJaAdicionado) {
                pedidoBanco.adicionarProduto(produtoBanco);
                pedidoRepository.save(pedidoBanco);
            }
            return true;
        }
        return false;
    }

    // Exclui o pedido pelo ID
    public void deletarPedidoPorId(Long id) {

        pedidoRepository.deleteById(id);
    }
}
