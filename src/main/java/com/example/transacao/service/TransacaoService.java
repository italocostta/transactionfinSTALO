package com.example.transacao.service;

import com.example.transacao.model.Transacao;
import com.example.transacao.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    public Transacao salvarTransacao(Transacao transacao) {
        // Salva a transação e retorna o objeto salvo
        return transacaoRepository.save(transacao);
    }

    public Optional<Transacao> buscarPorId(Long id) {
        return transacaoRepository.findById(id);
    }



    public void excluirTransacao(Long id) {
        Optional<Transacao> transacao = transacaoRepository.findById(id);
        transacao.ifPresent(transacaoRepository::delete);
    }

}
