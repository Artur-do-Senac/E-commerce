package com.example.ecommerceb2b.domain.repository;

import com.example.ecommerceb2b.domain.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> getPedidosByEmpresa_Id(Long empresa);
}
