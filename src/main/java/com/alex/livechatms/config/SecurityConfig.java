package com.alex.livechatms.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order; // Importar Order
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    // --- Cadeia de Filtros para Conteúdo Estático e Fluxo OAuth2 Client ---
    // Esta cadeia tem ordem mais alta (processada primeiro) e lida com recursos públicos
    // e o início do fluxo OAuth2 com GitHub.
    @Bean
    @Order(1) // Garante que esta cadeia seja processada antes da 'apiFilterChain'
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                // O securityMatcher deve ser o mais específico possível para esta cadeia.
                // Ele não deve interceptar /users ou /login, pois estes são endpoints de API (mesmo que públicos).
                .securityMatcher(
                        "/", "/index.html", "/login.js",
                        "/cadastro.html", "/cadastro.js", "/main.css",
                        "/oauth2/**" // Rotas para o fluxo OAuth2 Client (GitHub)

                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll() // Todas as requisições que correspondem ao securityMatcher são permitidas
                )
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/chat.html", true) // Redireciona para o chat após login bem-sucedido com GitHub
                        .loginPage("/index.html") // Sua página de login customizada
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    // --- Cadeia de Filtros para Endpoints de API (Públicos e Protegidos por JWT) ---
    // Esta cadeia irá interceptar todas as requisições que NÃO foram mapeadas pela 'webFilterChain'.
    // Ela lida com a autenticação de usuário/senha e a validação de JWT.
    @Bean
    @Order(2) // Processada depois da 'webFilterChain'
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso aos endpoints de registro e login (POST) - eles não exigem JWT
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // O WebSocket e outras APIs exigem autenticação JWT
                        .requestMatchers("/livechat-websocket/**").authenticated()
                        // Todas as outras requisições (incluindo /chat.html) exigem autenticação JWT
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs stateless (baseadas em JWT)
                // Habilita o OAuth2 Resource Server para validar tokens JWT nas requisições
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // Define a política de sessão como STATELESS para APIs protegidas por JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // !!! IMPORTANTE: Você precisa criar um bean que implemente org.springframework.security.core.userdetails.UserDetailsService
    // Este bean é responsável por carregar os detalhes do usuário (nome de usuário, senha criptografada, papéis)
    // do seu banco de dados para a autenticação tradicional de usuário/senha no endpoint /login.
    // Exemplo (você deve adaptar isso à sua entidade User e ao seu repositório):
    /*
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return username -> userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(), // A senha AQUI deve ser a senha JÁ CRIPTOGRAFADA do banco de dados
                    Collections.emptyList() // Ou carregue os papéis do usuário aqui (e.g., new SimpleGrantedAuthority("ROLE_USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
    */
}
