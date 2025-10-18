package com.expresso.finance.repository;

import com.expresso.finance.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; // Importe este

public interface CategoriaRepository extends JpaRepository<Categoria, String> {

    List<Categoria> findAllByClienteId(String clienteId);

    // MÉTODO QUE FALTA:
    // "Spring, encontre uma Categoria pelo 'nome' E pelo 'clienteId'"
    Optional<Categoria> findByNomeAndClienteId(String nome, String clienteId);
}