package com.example.transacao.service;

import com.example.transacao.model.Transacao;
import com.example.transacao.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAllByAtivoTrue();
    }

    public Transacao salvarTransacao(Transacao transacao, MultipartFile documento) {
        // Define a data de criação e status padrão
        transacao.setDataCriacao(LocalDateTime.now());
        transacao.setStatus(Transacao.StatusTransacao.EM_PROCESSAMENTO);

        // Verifica se um documento foi enviado
        if (documento != null && !documento.isEmpty()) {
            String nomeDocumento = salvarDocumento(documento);
            transacao.setDocumento(nomeDocumento);
        }

        return transacaoRepository.save(transacao);
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
    }

    public Transacao atualizarTransacao(Long id, Transacao transacaoAtualizada, MultipartFile documento) {
        Transacao transacaoExistente = buscarPorId(id);

        // Atualiza os dados da transação
        transacaoExistente.setValor(transacaoAtualizada.getValor());
        transacaoExistente.setStatus(transacaoAtualizada.getStatus());
        transacaoExistente.setDataAtualizacao(LocalDateTime.now());

        // Atualiza o documento se houver um novo
        if (documento != null && !documento.isEmpty()) {
            String nomeDocumento = salvarDocumento(documento);
            transacaoExistente.setDocumento(nomeDocumento);
        }

        return transacaoRepository.save(transacaoExistente);
    }

    public void excluirTransacao(Long id) {
        Transacao transacao = buscarPorId(id);
        transacao.setAtivo(false);
        transacaoRepository.save(transacao);
    }

    private String salvarDocumento(MultipartFile documento) {
        try {
            String diretorioUploads = "uploads/";
            Path caminhoDiretorioUploads = Paths.get(diretorioUploads);

            if (!Files.exists(caminhoDiretorioUploads)) {
                Files.createDirectories(caminhoDiretorioUploads);
            }

            Path caminhoDocumento = caminhoDiretorioUploads.resolve(Objects.requireNonNull(documento.getOriginalFilename()));
            Files.copy(documento.getInputStream(), caminhoDocumento, StandardCopyOption.REPLACE_EXISTING);

            return caminhoDocumento.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar documento", e);
        }
    }
}
