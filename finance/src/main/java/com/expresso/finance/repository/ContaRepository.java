package com.expresso.finance.repository;

import com.expresso.finance.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, String> {

    List<Conta> findByClienteId(String clienteId);

    @Query("SELECT SUM(c.saldoAtual) FROM Conta c WHERE c.cliente.id = :clienteId")
    BigDecimal calcularSaldoTotal(String clienteId);
}