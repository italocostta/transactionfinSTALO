package com.example.transacao.service;

import com.example.transacao.model.Transacao;
import com.example.transacao.model.Usuario;
import com.example.transacao.repository.TransacaoRepository;
import com.example.transacao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    public List<Transacao> listarTransacoesDoUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if ("ADMIN".equals(usuario.getRole())) {
            return transacaoRepository.findAllByAtivoTrue();
        } else {
            return transacaoRepository.findByUsuarioCriadorAndAtivoTrue(usuario);
        }
    }

    public Transacao salvarTransacao(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }

    public Transacao buscarPorId(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if ("ADMIN".equals(usuario.getRole()) || transacao.getUsuarioCriador().equals(usuario)) {
            return transacao;
        } else {
            throw new RuntimeException("Acesso negado");
        }
    }


    public void excluirTransacao(Long id) {
        Transacao transacao = buscarPorId(id);
        if (transacao != null) {
            transacao.setAtivo(false);
            transacaoRepository.save(transacao);
        }
    }

}
