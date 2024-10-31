package com.example.transacao.controller;

import com.example.transacao.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            String token = jwtUtil.generateToken(username);

            // Se a autenticação for bem-sucedida
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, String> result = new HashMap<>();
            result.put("message", "Login realizado com sucesso!");
            result.put("token", token);
            return result;
        } catch (AuthenticationException e) {
            // Se a autenticação falhar
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> result = new HashMap<>();
            result.put("error", "Login inválido. Verifique suas credenciais.");
            return result;
        }
    }
}
