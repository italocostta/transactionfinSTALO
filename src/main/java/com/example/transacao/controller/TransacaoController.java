package com.example.transacao.controller;

import com.example.transacao.model.Transacao;
import com.example.transacao.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping("/transacoes")
    public List<Transacao> listarTransacoes() {
        return transacaoService.listarTodas();
    }

    @PostMapping("/transacoes")
    public ResponseEntity<?> criarTransacao(
            @RequestPart("transacao") Transacao transacao,
            @RequestPart(value = "documento", required = false) MultipartFile documento) {
        try {
            Transacao novaTransacao = transacaoService.salvarTransacao(transacao, documento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF já está em uso para uma transação em andamento. Aguarde a conclusão da mesma.");
        }
    }

    @GetMapping("/transacoes/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Transacao transacao = transacaoService.buscarPorId(id);
            return ResponseEntity.ok(transacao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transação não encontrada");
        }
    }

    @PutMapping("/transacoes/{id}")
    public ResponseEntity<?> atualizarTransacao(
            @PathVariable Long id,
            @RequestPart("transacao") Transacao transacaoAtualizada,
            @RequestPart(value = "documento", required = false) MultipartFile documento) {
        try {
            Transacao transacao = transacaoService.atualizarTransacao(id, transacaoAtualizada, documento);
            return ResponseEntity.ok(transacao);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar transação.");
        }
    }

    @DeleteMapping("/transacoes/{id}")
    public ResponseEntity<Void> excluirTransacao(@PathVariable Long id) {
        try {
            transacaoService.excluirTransacao(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
