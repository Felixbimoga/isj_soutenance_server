package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.FeedbackDTO;
import com.EasayHelp.EasayHelp.dto.ReclamationDTO;
import com.EasayHelp.EasayHelp.entity.Reclamation;

import java.util.List;

public interface ReclamationService {

    Reclamation createReclamation(ReclamationDTO reclamationDTO, Long commande_id);

    List<ReclamationDTO> getAllReclamation();

    ReclamationDTO getReclamationById(Long reclamationId);

    // Ajouter la signature de la méthode de mise à jour
    int updateReclamation(ReclamationDTO reclamationDTO);

    List<ReclamationDTO> getReclamationByCommandeId(Long commande_id);

    void deleteReclamation(Long reclamationDTO);
}
