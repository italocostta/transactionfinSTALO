package com.example.transacao.repository;

import com.example.transacao.model.Transacao;
import com.example.transacao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t WHERE t.ativo = true")
    List<Transacao> findAllByAtivoTrue();

    @Query("SELECT t FROM Transacao t WHERE t.usuarioCriador = :usuario AND t.ativo = true")
    List<Transacao> findByUsuarioCriadorAndAtivoTrue(@Param("usuario") Usuario usuario);
}
