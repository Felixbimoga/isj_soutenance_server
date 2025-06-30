package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande,Long> {

    List<Commande> findByUtilisateurId(Long utilisateurid);

    // Toutes les commandes assignées à un technicien donné
    List<Commande> findByTechnicienId(Long technicienId);

    List<Commande> findAllByServiceId(Long serviceid);

    // Commandes en attente pour un technicien spécifique
    List<Commande> findByTechnicienIdAndStatut(Long technicienId, String statut);

    // Commandes d’un client avec un certain statut
    List<Commande> findByUtilisateurIdAndStatut(Long utilisateurId, String statut);

}
