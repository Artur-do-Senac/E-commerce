package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.EnumStatusProduto;
import com.example.ecommerceb2b.application.DTOs.ProdutoRequest;
import com.example.ecommerceb2b.application.DTOs.ProdutoResponse;
import com.example.ecommerceb2b.application.DTOs.UsuarioLogado;
import com.example.ecommerceb2b.domain.entities.Empresa;
import com.example.ecommerceb2b.domain.entities.Produto;
import com.example.ecommerceb2b.domain.repository.EmpresaRepository;
import com.example.ecommerceb2b.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Camada de Application: concentra a lógica dos produtos e conversa com o Repository (banco de dados).
// A ProdutoController só chama os métodos daqui.
@Service
public class ProdutoService {

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

    // Retorna os produtos da empresa de quem está logado. O ADMIN enxerga os de todas as empresas
    public List<ProdutoResponse> listarTodos() {

        var usuarioLogado = UsuarioLogado.atual();

        List<Produto> produtos;

        if (usuarioLogado.admin()) {
            produtos = produtoRepository.findAll();
        } else if (usuarioLogado.empresa_id() == null) {
            produtos = List.of();
        } else {
            produtos = produtoRepository.getProdutosByEmpresa_Id(usuarioLogado.empresa_id());
        }

        return produtos.stream()
                .map(ProdutoResponse::new)
                .toList();
    }

    // Busca um produto pelo ID. Retorna o ProdutoResponse (nunca a entidade) ou null quando não existe ou é de outra empresa (a Controller transforma o null em 404)
    public ProdutoResponse buscarPorId(Long id) {

        var produto = produtoRepository.findById(id).orElse(null);

        if (produto == null || !UsuarioLogado.atual().podeAcessar(produto.getEmpresa())) {
            return null;
        }
        return new ProdutoResponse(produto);
    }

    // Salva o produto: com id 0 cria um novo (da empresa de quem está logado), com qualquer outro id atualiza o existente. Retorna o produto salvo ou null quando o ID não existe ou é de outra empresa
    // Dados inválidos (preço, estoque...) lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public ProdutoResponse salvarProduto(ProdutoRequest produto, Long id) {

        // Novo produto
        if (id == 0L) {
            Produto produtoBanco = new Produto(produto);
            produtoBanco.setEmpresa(getEmpresaUsuarioLogado());
            produtoRepository.save(produtoBanco);

            return new ProdutoResponse(produtoBanco);
        }

        // Produto existente
        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null && UsuarioLogado.atual().podeAcessar(produtoBanco.getEmpresa())) {
            produtoBanco.setNome(produto.nome());
            produtoBanco.setPreco(produto.preco());
            produtoBanco.setVencimento(produto.vencimento());
            produtoBanco.setQuantidadeEstoque(produto.quantidadeEstoque());
            produtoRepository.save(produtoBanco);

            return new ProdutoResponse(produtoBanco);
        }
        return null;
    }

    // Atualiza somente o status do produto. Retorna true quando atualizou e false quando o ID não existe ou é de outra empresa
    public boolean atualizarStatus(EnumStatusProduto status, Long id) {

        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null && UsuarioLogado.atual().podeAcessar(produtoBanco.getEmpresa())) {
            produtoBanco.setStatus(status);
            produtoRepository.save(produtoBanco);

            return true;
        }
        return false;
    }

    // Exclui o produto pelo ID
    public void deletarProdutoPorId(Long id) {

        produtoRepository.deleteById(id);
    }
}
