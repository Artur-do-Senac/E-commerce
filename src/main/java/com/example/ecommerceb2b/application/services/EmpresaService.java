package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.EmpresaRequest;
import com.example.ecommerceb2b.application.DTOs.EmpresaResponse;
import com.example.ecommerceb2b.application.DTOs.EnumStatusEmpresa;
import com.example.ecommerceb2b.domain.entities.Categoria;
import com.example.ecommerceb2b.domain.entities.Empresa;
import com.example.ecommerceb2b.domain.repository.CategoriaRepository;
import com.example.ecommerceb2b.domain.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Camada de Application: concentra a lógica das empresas e conversa com o Repository (banco de dados).
// A EmpresaController só chama os métodos daqui.
@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Retorna todas as empresas cadastradas
    public List<EmpresaResponse> listarTodos() {

        return empresaRepository.findAll()
                .stream()
                .map(EmpresaResponse::new)
                .toList();
    }

    // Busca uma empresa pelo ID. Retorna o EmpresaResponse (nunca a entidade) ou null quando não existe (a Controller transforma o null em 404)
    public EmpresaResponse buscarPorId(Long id) {

        var empresa = empresaRepository.findById(id).orElse(null);

        if (empresa == null) {
            return null;
        }
        return new EmpresaResponse(empresa);
    }

    // Salva a empresa: com id 0 cria uma nova, com qualquer outro id atualiza a existente. Retorna a empresa salva ou null quando o ID não existe
    // Dados inválidos (CNPJ, telefone, categoria...) lançam IllegalArgumentException (a Controller transforma em 400 com a mensagem)
    public EmpresaResponse salvarEmpresa(EmpresaRequest empresa, Long id) {

        // Categoria (ramo) escolhida. Se não existir, fica null e a entidade recusa com "Categoria Invalida!"
        Categoria categoria = empresa.categoriaId() == null ? null : categoriaRepository.findById(empresa.categoriaId()).orElse(null);

        // Nova empresa
        if (id == 0L) {
            Empresa empresaBanco = new Empresa(empresa, categoria);
            empresaRepository.save(empresaBanco);

            return new EmpresaResponse(empresaBanco);
        }

        // Empresa existente
        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            empresaBanco.setRazaoSocial(empresa.razaoSocial());
            empresaBanco.setCnpj(empresa.cnpj());
            empresaBanco.informarTelefone(empresa.telefone());
            empresaBanco.setCategoria(categoria);
            empresaRepository.save(empresaBanco);

            return new EmpresaResponse(empresaBanco);
        }
        return null;
    }

    // Atualiza somente o status da empresa. Retorna true quando atualizou e false quando o ID não existe
    public boolean atualizarStatus(EnumStatusEmpresa status, Long id) {

        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            empresaBanco.setStatus(status);
            empresaRepository.save(empresaBanco);

            return true;
        }
        return false;
    }

    // Exclui a empresa pelo ID
    public void deletarEmpresaPorId(Long id) {

        empresaRepository.deleteById(id);
    }
}
