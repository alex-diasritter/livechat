package com.alex.livechatms.service;
import com.alex.livechatms.domain.entities.User;
import com.alex.livechatms.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections; // Importar Collections
import java.util.List; // Importar List

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        // Adaptação para getRole() que retorna uma única Role
        // Se o usuário tiver um papel (role) atribuído, crie uma lista com ele.
        // Caso contrário, retorne uma lista vazia.
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();
        if (user.getRole() != null) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities // Usa a lista de autoridades criada
        );
    }
}
