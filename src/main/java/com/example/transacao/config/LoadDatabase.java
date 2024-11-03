package com.example.transacao.config;

import com.example.transacao.model.Role;
import com.example.transacao.model.Usuario;
import com.example.transacao.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LoadDatabase {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            if (repository.findByEmail("admin@exemplo.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@exemplo.com");
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setNome("Admin");
                admin.setAtivo(true);
                admin.setRole(Role.ADMIN);
                repository.save(admin);
            }

        };
    }
}
