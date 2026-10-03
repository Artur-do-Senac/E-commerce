package com.example.ecommerceb2b.domain.repository;

import com.example.ecommerceb2b.domain.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
