package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Enum.Role;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findByMail(String mail);

    // Nouvelle méthode pour récupérer les utilisateurs selon leur rôle
    List<Utilisateur> findByRole(Role role);

    // Variante 1 : filtrer aussi par rôle
    List<Utilisateur> findByRoleAndService_Id(Role role, Long serviceId);

    // Variante 2 : si « TECHNICIEN » suffit déjà à les distinguer
    List<Utilisateur> findByService_Id(Long serviceId);
}
