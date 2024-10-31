package com.example.transacao.controller;

import com.example.transacao.model.Transacao;
import com.example.transacao.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping
    public List<Transacao> listarTodas() {
        return transacaoService.listarTodas();
    }

    @PostMapping
    public Transacao criarTransacao(@RequestBody Transacao transacao) {
        return transacaoService.salvarTransacao(transacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        Transacao transacao = transacaoService.buscarPorId(id);
        if (transacao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTransacao(@PathVariable Long id) {
        Transacao transacao = transacaoService.buscarPorId(id);
        if (transacao == null) {
            return ResponseEntity.notFound().build();
        }
        transacaoService.excluirTransacao(id);
        return ResponseEntity.noContent().build();
    }
}
