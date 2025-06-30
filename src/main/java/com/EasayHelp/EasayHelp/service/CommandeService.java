package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.CommandeDTO;
import com.EasayHelp.EasayHelp.entity.Commande;

import java.util.List;

public interface CommandeService {

    // Créer une nouvelle commande
    Commande createCommande(CommandeDTO commandeDTO);

    // Obtenir toutes les commandes
    List<CommandeDTO> getAllCommande();

    // Obtenir une commande par son ID
    CommandeDTO getCommandeById(Long Id);

    // Obtenir les commandes d’un utilisateur
    List<CommandeDTO> getCommandeByUser(Long userId);

    //attribuer une commande à un technicien
    CommandeDTO assignerCommandeAuTechnicien(Long commandeId, Long technicienId);

    //modifier le statut d'une commande
    CommandeDTO modifierStatutCommande(Long commandeId, String nouveauStatut);

    // Obtenir les commandes d’un technicien
    List<CommandeDTO> getCommandeByTechnicien(Long technicienId);

    // Obtenir les commandes associées à un service
    List<CommandeDTO> getCommandeByService(Long serviceId);

    // Ajouter la signature de la méthode de mise à jour
    int updateCommande(CommandeDTO commandeDTO);

    // Supprimer une commande
    void deleteCommande(Long commandeId);

}
