package com.alex.livechatms.config;

import com.alex.livechatms.domain.entities.Role;
import com.alex.livechatms.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    // Este bean será executado uma vez que a aplicação Spring Boot for iniciada.
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Role basicRole = new Role();
            basicRole.setName(Role.Values.BASIC.name()); // Define o nome como "BASIC"
            roleRepository.save(basicRole);
            System.out.println("Role 'BASIC' criada no startup.");
        };
    }
}
