package com.alex.livechatms.controller;

import com.alex.livechatms.domain.CreateUserDto;
import com.alex.livechatms.domain.entities.Role;
import com.alex.livechatms.domain.entities.User;
import com.alex.livechatms.repository.RoleRepository;
import com.alex.livechatms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional; // Importar Optional
// import java.util.Set; // Remova esta importação se não for mais utilizada

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {

        System.out.println("DEBUG: Iniciando newUser para username: " + dto.username());

        // Procura pela role "BASIC".
        Optional<Role> basicRoleOptional = roleRepository.findByName(Role.Values.BASIC.name());
        Role basicRole;

        if (basicRoleOptional.isPresent()) {
            basicRole = basicRoleOptional.get();
            System.out.println("DEBUG: Role 'BASIC' encontrada no DB: " + basicRole.getName() + " (ID: " + basicRole.getRoleId() + ")");
        } else {
            System.out.println("DEBUG: Role 'BASIC' não encontrada no DB, tentando criar e salvar.");
            Role newRole = new Role();
            newRole.setName(Role.Values.BASIC.name());
            basicRole = roleRepository.save(newRole); // Salva a nova role no DB
            System.out.println("DEBUG: Role 'BASIC' criada e salva via UserController: " + basicRole.getName() + " (ID: " + basicRole.getRoleId() + ")");
        }

        System.out.println("DEBUG: Valor final de basicRole antes de setRole(): " + (basicRole == null ? "NULL" : basicRole.getName() + " (ID: " + basicRole.getRoleId() + ")"));

        var userFromDb = userRepository.findByUsername(dto.username());
        if (userFromDb.isPresent()) {
            System.out.println("DEBUG: Usuário " + dto.username() + " já existe.");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Usuário já existe.");
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        // LINHA 50: CORRIGIDO de setRoles(Set.of(basicRole)) para setRole(basicRole)
        user.setRole(basicRole);

        userRepository.save(user);
        System.out.println("DEBUG: Usuário " + dto.username() + " salvo com sucesso.");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
