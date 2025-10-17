package com.expresso.finance.repository;

import com.expresso.finance.entity.Categoria;
import com.expresso.finance.entity.Cliente;
import com.expresso.finance.entity.Conta;
import com.expresso.finance.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, String> {

    List<Transacao> findByCliente(Cliente cliente);

    List<Transacao> findByClienteAndConta(Cliente cliente, Conta conta);

    List<Transacao> findByClienteAndCategoria(Cliente cliente, Categoria categoria);

    List<Transacao> findTop10ByClienteOrderByDataOperacaoDesc(Cliente cliente);

    @Query("SELECT t FROM Transacao t WHERE t.cliente = :cliente AND t.dataOperacao BETWEEN :dataInicio AND :dataFim")
    List<Transacao> findByClienteAndPeriodo(
            @Param("cliente") Cliente cliente,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT t FROM Transacao t WHERE t.cliente = :cliente AND t.conta = :conta AND t.dataOperacao BETWEEN :dataInicio AND :dataFim")
    List<Transacao> findByClienteContaAndPeriodo(
            @Param("cliente") Cliente cliente,
            @Param("conta") Conta conta,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT t FROM Transacao t WHERE t.cliente = :cliente AND t.categoria = :categoria AND t.dataOperacao BETWEEN :dataInicio AND :dataFim")
    List<Transacao> findByClienteCategoriaAndPeriodo(
            @Param("cliente") Cliente cliente,
            @Param("categoria") Categoria categoria,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.cliente = :cliente AND t.valor > 0 AND t.dataOperacao BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalReceitas(
            @Param("cliente") Cliente cliente,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.cliente = :cliente AND t.valor < 0 AND t.dataOperacao BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalDespesas(
            @Param("cliente") Cliente cliente,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT t.categoria, SUM(t.valor) FROM Transacao t WHERE t.cliente = :cliente AND t.valor < 0 AND t.dataOperacao BETWEEN :dataInicio AND :dataFim GROUP BY t.categoria")
    List<Object[]> calcularGastosPorCategoria(
            @Param("cliente") Cliente cliente,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}
