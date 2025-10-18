package com.expresso.finance.repository;

import com.expresso.finance.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MetaRepository extends JpaRepository<Meta, String> {

    // "Spring, encontre todas as Metas pelo 'clienteId'"
    List<Meta> findAllByClienteId(String clienteId);
}