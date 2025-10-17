package com.expresso.finance.repository;

import com.expresso.finance.entity.Cliente;
import com.expresso.finance.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, String> {

    List<Meta> findByCliente(Cliente cliente);

    List<Meta> findByClienteAndFixadaTrue(Cliente cliente);

    @Query("SELECT m FROM Meta m WHERE m.cliente = :cliente AND m.prazo <= :dataLimite ORDER BY m.prazo ASC")
    List<Meta> findMetasProximasConclusao(@Param("cliente") Cliente cliente, @Param("dataLimite") LocalDate dataLimite);

    @Query("SELECT m FROM Meta m WHERE m.cliente = :cliente ORDER BY m.progresso DESC")
    List<Meta> findMetasOrdenadaPorProgresso(@Param("cliente") Cliente cliente);
}
