package com.example.transacao.controller;

import com.example.transacao.model.Transacao;
import com.example.transacao.model.Usuario;
import com.example.transacao.repository.UsuarioRepository;
import com.example.transacao.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransacaoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransacaoService transacaoService;

    @GetMapping("/transacoes")
    public List<Transacao> listarTodas() {
        return transacaoService.listarTodas();
    }

    @PostMapping("/transacoes")
    public ResponseEntity<?> criarTransacao(@RequestBody Transacao transacao) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();

        Usuario usuarioCriador = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        transacao.setUsuarioCriador(usuarioCriador);

        transacao.setStatus(Transacao.StatusTransacao.EM_PROCESSAMENTO);

        try {
            Transacao novaTransacao = transacaoService.salvarTransacao(transacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF já está em uso para uma transação em andamento. Aguarde a conclusão da mesma.");
        }
    }

    @GetMapping("/transacoes/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Transacao> transacao = Optional.ofNullable(transacaoService.buscarPorId(id));
        if (transacao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transação não encontrada");
        }
        return ResponseEntity.ok(transacao.get());
    }



    @DeleteMapping("/transacoes/{id}")
    public ResponseEntity<Void> excluirTransacao(@PathVariable Long id) {
        Transacao transacao = transacaoService.buscarPorId(id);
        if (transacao == null) {
            return ResponseEntity.notFound().build();
        }
        transacaoService.excluirTransacao(id);
        return ResponseEntity.noContent().build(); // Retorna 204 após soft delete
    }
}
