package com.example.transacao.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private String usuarioCriador;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacao status;

    public enum StatusTransacao {
        EM_PROCESSAMENTO,
        APROVADA,
        NEGADA
    }
}
