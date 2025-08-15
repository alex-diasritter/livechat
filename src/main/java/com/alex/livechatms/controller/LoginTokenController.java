package com.alex.livechatms.controller;

import com.alex.livechatms.domain.LoginRequest;
import com.alex.livechatms.domain.LoginResponse;
import com.alex.livechatms.domain.entities.Role;
import com.alex.livechatms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.stream.Collectors; // Ainda necessário se você planeja usar para outras coleções, mas não para a 'Role' aqui.

@RestController
public class LoginTokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public LoginTokenController(JwtEncoder jwtEncoder,
                                UserRepository userRepository,
                                BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByUsername(loginRequest.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválidos!");
        }

        var now = Instant.now();
        var expiresIn = 300L; // Token válido por 300 segundos (5 minutos)

        // Adaptação para o método getRole() que retorna uma única Role
        String scopes = ""; // Inicializa como string vazia por padrão
        if (user.get().getRole() != null) {
            // Obtém o nome da Role.
            // Se você quiser seguir a convenção de "ROLE_" também no claim do JWT, adicione aqui.
            // Exemplo: scopes = "ROLE_" + user.get().getRole().getName();
            scopes = user.get().getRole().getName();
        }

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend") // Emissor do JWT
                .subject(user.get().getUserId().toString()) // Sujeito do JWT (ID do usuário)
                .issuedAt(now) // Data de emissão
                .expiresAt(now.plusSeconds(expiresIn)) // Data de expiração
                .claim("scope", scopes) // Adiciona o(s) escopo(s)/papel(is) como claim
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
