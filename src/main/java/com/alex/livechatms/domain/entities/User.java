package com.alex.livechatms.domain.entities;

import com.alex.livechatms.domain.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID; // Remova Set se não for mais usado

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(unique = true, nullable = false) // Adicionado nullable = false para username
    private String username;

    @Column(nullable = false) // Adicionado nullable = false para password
    private String password;

    // Alterado de @ManyToMany para @ManyToOne
    // Um usuário tem UMA role, mas UMA role pode pertencer a MÚLTIPLOS usuários
    @ManyToOne(fetch = FetchType.EAGER) // FetchType.EAGER carrega a role junto com o usuário
    @JoinColumn(name = "role_id", nullable = false) // Coluna que armazena o ID da role na tabela tb_users
    private Role role; // Alterado de Set<Role> para uma única Role

    // Construtor padrão (necessário para JPA)
    public User() {}

    // Getters e Setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter para a única Role
    public Role getRole() {
        return role;
    }

    // Setter para a única Role
    public void setRole(Role role) { // O tipo do parâmetro deve ser Role, não String
        this.role = role;
    }

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        // Garante que nem a senha do request nem a senha do usuário sejam nulas antes de comparar
        if (loginRequest.password() == null || this.password == null) {
            return false;
        }
        return passwordEncoder.matches(loginRequest.password(), this.password);
    }
}
