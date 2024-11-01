package com.example.transacao.repository;

import com.example.transacao.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    @Query("SELECT t FROM Transacao t WHERE t.ativo = true")        // Retorna apenas as transações que estão ativas
    List<Transacao> findAllAtivas();
}
