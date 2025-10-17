package com.expresso.finance.repository;

import com.expresso.finance.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    List<Categoria> findByClienteId(String clienteId);

    Optional<Categoria> findByNomeAndClienteId(String nome, String clienteId);

    boolean existsByNomeAndClienteId(String nome, String clienteId);
}