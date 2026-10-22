package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.EnumStatusProduto;
import com.example.ecommerceb2b.application.DTOs.ProdutoRequest;
import com.example.ecommerceb2b.application.DTOs.ProdutoResponse;
import com.example.ecommerceb2b.domain.entities.Produto;
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

    // Retorna todos os produtos cadastrados
    public List<ProdutoResponse> listarTodos() {

        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponse::new)
                .toList();
    }

    // Busca um produto pelo ID. Retorna o ProdutoResponse (nunca a entidade) ou null quando não existe (a Controller transforma o null em 404)
    public ProdutoResponse buscarPorId(Long id) {

        var produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            return null;
        }
        return new ProdutoResponse(produto);
    }

    // Salva o produto: com id 0 cria um novo, com qualquer outro id atualiza o existente. Retorna o produto salvo ou null quando o ID não existe
    // Dados inválidos (preço, estoque...) lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public ProdutoResponse salvarProduto(ProdutoRequest produto, Long id) {

        // Novo produto
        if (id == 0L) {
            Produto produtoBanco = new Produto(produto);
            produtoRepository.save(produtoBanco);

            return new ProdutoResponse(produtoBanco);
        }

        // Produto existente
        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null) {
            produtoBanco.setNome(produto.nome());
            produtoBanco.setPreco(produto.preco());
            produtoBanco.setVencimento(produto.vencimento());
            produtoBanco.setQuantidadeEstoque(produto.quantidadeEstoque());
            produtoRepository.save(produtoBanco);

            return new ProdutoResponse(produtoBanco);
        }
        return null;
    }

    // Atualiza somente o status do produto. Retorna true quando atualizou e false quando o ID não existe
    public boolean atualizarStatus(EnumStatusProduto status, Long id) {

        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null) {
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
