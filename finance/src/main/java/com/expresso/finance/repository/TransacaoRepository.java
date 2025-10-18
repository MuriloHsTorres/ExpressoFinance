package com.expresso.finance.repository;

import com.expresso.finance.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, String> {

    // "Spring, encontre todas as Transacoes pelo 'clienteId'"
    List<Transacao> findAllByClienteId(String clienteId);

    // "Spring, encontre todas as Transacoes pela 'contaId'"
    // (Útil para quando o utilizador quiser ver o extrato de uma conta)
    List<Transacao> findAllByContaId(String contaId);
}