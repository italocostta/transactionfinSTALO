package com.example.transacao.config;

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
                admin.setRole("ADMIN");
                repository.save(admin);
            }

            if (repository.findByEmail("user@exemplo.com").isEmpty()) {
                Usuario user = new Usuario();
                user.setEmail("user@exemplo.com");
                user.setSenha(passwordEncoder.encode("1234567"));
                user.setNome("User");
                user.setAtivo(true);
                user.setRole("USER");
                repository.save(user);
            }

            if (repository.findByEmail("user2@exemplo.com").isEmpty()) {
                Usuario user = new Usuario();
                user.setEmail("user2@exemplo.com");
                user.setSenha(passwordEncoder.encode("12345678"));
                user.setNome("User2");
                user.setAtivo(true);
                user.setRole("USER");
                repository.save(user);
            }
        };
    }
}
