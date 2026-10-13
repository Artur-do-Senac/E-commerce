package com.example.ecommerceb2b.application.services;

import com.example.ecommerceb2b.application.DTOs.AttStatusEmpresaRequest;
import com.example.ecommerceb2b.domain.entities.Empresa;
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

    // Retorna todas as empresas cadastradas
    public List<Empresa> listarTodos() {

        return empresaRepository.findAll();
    }

    // Busca uma empresa pelo ID. Retorna a empresa encontrada ou null quando não existe (a Controller transforma o null em 404)
    public Empresa buscarEmpresaPorIdRetornaEmpresaOuNulo(Long id) {

        return empresaRepository.findById(id).orElse(null);
    }

    // Salva uma nova empresa no banco e retorna a empresa já salva (com o ID gerado)
    public Empresa criarEmpresaRetornaEmpresaCriada(Empresa empresa) {

        return empresaRepository.save(empresa);
    }

    // Atualiza somente o status da empresa. Retorna a empresa atualizada ou null quando o ID não existe
    public Empresa atualizarStatusEmpresaRetornaEmpresaOuNulo(Long id, AttStatusEmpresaRequest statusRequest) {

        Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

        if (empresaBanco != null) {
            empresaBanco.setStatus(statusRequest.statusEmpresa());

            return empresaRepository.save(empresaBanco);
        }
        return null;
    }

    // Atualiza todos os dados da empresa. Retorna a empresa atualizada ou null quando o ID não existe
    public Empresa atualizarEmpresaRetornaEmpresaOuNulo(Long id, Empresa empresa) {

        try {
            Empresa empresaBanco = empresaRepository.findById(id).orElse(null);

            if (empresaBanco != null) {
                empresaBanco.setStatus(empresa.getStatus());
                empresaBanco.setRazaoSocial(empresa.getRazaoSocial());
                empresaBanco.setCnpj(empresa.getCnpj());
                empresaBanco.setCategoria(empresa.getCategoria());

                return empresaRepository.save(empresaBanco);
            }
            return null;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // Exclui a empresa pelo ID
    public void deletarEmpresaPorId(Long id) {

        empresaRepository.deleteById(id);
    }
}
