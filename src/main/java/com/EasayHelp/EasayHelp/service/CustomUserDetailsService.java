package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.entity.Enum.Role;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import com.EasayHelp.EasayHelp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByMail(mail);

        if (user == null){
            throw new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur :" +mail);
        }

        if (user.getRole() == null) {
            throw new UsernameNotFoundException("L'utilisateur n'a pas de rôle attribué");
        }

        System.out.println("loadUserByUsername");
        System.out.println(user.getRole().name());
        System.out.println("enabled: " + user.isEnabled()); // debug ici
        return new org.springframework.security.core.userdetails.User(
                user.getMail(), // email comme identifiant
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public List<Utilisateur> findAll() {
        return this.userRepository.findAll();
    }

    public List<Utilisateur> getTechniciensByService(Long serviceId) {
        return userRepository.findByRoleAndService_Id(Role.TECHNICIEN, serviceId);
    }

    // Méthode pour récupérer la photo de l'utilisateur
    public byte[] getUserPhotoById(Long id) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return user.getPhoto();
    }

    public Utilisateur getUtilisateurById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));
    }

    public List<Utilisateur> getUsersByRole(String role) {
        Role enumRole = Role.valueOf(role.toUpperCase()); // Convertit la chaîne en enum
        return userRepository.findByRole(enumRole);
    }

    public Utilisateur blockUser(Long userId) {
        Utilisateur user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setEnabled(false);
        System.out.println("Enabled status before save: " + user.isEnabled());
        return userRepository.save(user);
    }

    public Utilisateur unblockUser(Long userId) {
        Utilisateur user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setEnabled(true);
        return userRepository.save(user);
    }

}
