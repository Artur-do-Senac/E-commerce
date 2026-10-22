package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.CategoriaRequest;
import com.example.ecommerceb2b.application.DTOs.CategoriaResponse;
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
    public List<CategoriaResponse> listarTodos() {

        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponse::new)
                .toList();
    }

    // Busca uma categoria pelo ID. Retorna o CategoriaResponse (nunca a entidade) ou null quando não existe (a Controller transforma o null em 404)
    public CategoriaResponse buscarPorId(Long id) {

        var categoria = categoriaRepository.findById(id).orElse(null);

        if (categoria == null) {
            return null;
        }
        return new CategoriaResponse(categoria);
    }

    // Salva a categoria: com id 0 cria uma nova, com qualquer outro id atualiza a existente. Retorna a categoria salva ou null quando o ID não existe
    // Dados inválidos lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public CategoriaResponse salvarCategoria(CategoriaRequest categoria, Long id) {

        // Nova categoria
        if (id == 0L) {
            Categoria categoriaBanco = new Categoria(categoria);
            categoriaRepository.save(categoriaBanco);

            return new CategoriaResponse(categoriaBanco);
        }

        // Categoria existente
        Categoria categoriaBanco = categoriaRepository.findById(id).orElse(null);

        if (categoriaBanco != null) {
            categoriaBanco.setNome(categoria.nome());
            categoriaBanco.setDescricao(categoria.descricao());
            categoriaRepository.save(categoriaBanco);

            return new CategoriaResponse(categoriaBanco);
        }
        return null;
    }

    // Exclui a categoria pelo ID
    public void deletarCategoriaPorId(Long id) {

        categoriaRepository.deleteById(id);
    }
}
