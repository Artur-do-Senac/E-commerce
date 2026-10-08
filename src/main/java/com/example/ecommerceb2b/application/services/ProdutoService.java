package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.AttStatusProdutoRequest;
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
    public List<Produto> listarTodos() {

        return produtoRepository.findAll();
    }

    // Busca um produto pelo ID. Retorna o produto encontrado ou null quando não existe (a Controller transforma o null em 404)
    public Produto buscarProdutoPorIdRetornaProdutoOuNulo(Long id) {

        return produtoRepository.findById(id).orElse(null);
    }

    // Salva um novo produto no banco e retorna o produto já salvo (com o ID gerado)
    public Produto criarProdutoRetornaProdutoCriado(Produto produto) {

        return produtoRepository.save(produto);
    }

    // Atualiza somente o status do produto. Retorna o produto atualizado ou null quando o ID não existe
    public Produto atualizarStatusProdutoRetornaProdutoOuNulo(Long id, AttStatusProdutoRequest statusRequest) {

        Produto produtoBanco = produtoRepository.findById(id).orElse(null);

        if (produtoBanco != null) {
            produtoBanco.setStatus(statusRequest.statusProduto());

            return produtoRepository.save(produtoBanco);
        }
        return null;
    }

    // Atualiza todos os dados do produto. Retorna o produto atualizado ou null quando o ID não existe
    public Produto atualizarProdutoRetornaProdutoOuNulo(Long id, Produto produto) {

        try {
            Produto produtoBanco = produtoRepository.findById(id).orElse(null);

            if (produtoBanco != null) {
                produtoBanco.setStatus(produto.getStatus());
                produtoBanco.setNome(produto.getNome());
                produtoBanco.setPreco(produto.getPreco());
                produtoBanco.setVencimento(produto.getVencimento());
                produtoBanco.setQuantidadeEstoque(produto.getQuantidadeEstoque());

                return produtoRepository.save(produtoBanco);
            }
            return null;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // Exclui o produto pelo ID
    public void deletarProdutoPorId(Long id) {

        produtoRepository.deleteById(id);
    }
}
