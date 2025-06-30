package com.EasayHelp.EasayHelp.initializer;

import com.EasayHelp.EasayHelp.entity.Enum.Role;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import com.EasayHelp.EasayHelp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.findByMail("admin@gmail.com") == null) {


            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setMail("admin@gmail.com");
            admin.setTelephone("+237 690 123 456");

            userRepository.save(admin);
        }
    }
}
