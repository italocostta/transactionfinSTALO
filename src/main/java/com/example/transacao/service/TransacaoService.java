package com.example.transacao.service;

import com.example.transacao.model.Transacao;
import com.example.transacao.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    public Transacao salvarTransacao(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id).orElse(null);
    }

    public void excluirTransacao(Long id) {
        Transacao transacao = buscarPorId(id);
        if (transacao != null) {
            transacaoRepository.delete(transacao);
        }
    }
}
