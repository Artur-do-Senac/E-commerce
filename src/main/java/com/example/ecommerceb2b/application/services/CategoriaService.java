package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.domain.entities.Categoria;
import com.example.ecommerceb2b.domain.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Camada de Application: concentra a lógica das categorias e conversa com o Repository (banco de dados).
// A CategoriaController só chama os métodos daqui.
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Salva uma nova categoria no banco e retorna a categoria já salva (com o ID gerado)
    public Categoria criarCategoriaRetornaCategoriaCriada(Categoria categoria) {

        return categoriaRepository.save(categoria);
    }
}
