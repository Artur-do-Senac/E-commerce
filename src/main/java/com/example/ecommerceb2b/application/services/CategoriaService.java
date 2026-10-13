package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.domain.entities.Categoria;
import com.example.ecommerceb2b.domain.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Camada de Application: concentra a lógica das categorias e conversa com o Repository (banco de dados).
// A CategoriaController só chama os métodos daqui.
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Retorna todas as categorias cadastradas
    public List<Categoria> listarTodos() {

        return categoriaRepository.findAll();
    }

    // Busca uma categoria pelo ID. Retorna a categoria encontrada ou null quando não existe (a Controller transforma o null em 404)
    public Categoria buscarCategoriaPorIdRetornaCategoriaOuNulo(Long id) {

        return categoriaRepository.findById(id).orElse(null);
    }

    // Salva uma nova categoria no banco e retorna a categoria já salva (com o ID gerado)
    public Categoria criarCategoriaRetornaCategoriaCriada(Categoria categoria) {

        return categoriaRepository.save(categoria);
    }

    // Atualiza todos os dados da categoria. Retorna a categoria atualizada ou null quando o ID não existe
    public Categoria atualizarCategoriaRetornaCategoriaOuNulo(Long id, Categoria categoria) {

        Categoria categoriaBanco = categoriaRepository.findById(id).orElse(null);

        if (categoriaBanco != null) {
            categoriaBanco.setNome(categoria.getNome());
            categoriaBanco.setDescricao(categoria.getDescricao());

            return categoriaRepository.save(categoriaBanco);
        }
        return null;
    }

    // Exclui a categoria pelo ID
    public void deletarCategoriaPorId(Long id) {

        categoriaRepository.deleteById(id);
    }
}
