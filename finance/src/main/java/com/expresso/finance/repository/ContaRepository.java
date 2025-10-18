package com.expresso.finance.repository;

import com.expresso.finance.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Importe esta classe

public interface ContaRepository extends JpaRepository<Conta, String> {

    // NOVO MÉTODO:
    // "Spring, me encontre todas as Contas pelo campo 'cliente'
    //  e use o 'Id' desse cliente para filtrar"
    List<Conta> findAllByClienteId(String clienteId);
}