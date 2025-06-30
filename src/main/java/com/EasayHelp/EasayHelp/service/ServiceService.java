package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.ServiceDTO;
import com.EasayHelp.EasayHelp.entity.Service;
import com.EasayHelp.EasayHelp.entity.Enum.StatutService;

import java.util.List;

public interface ServiceService {


    Service createService(ServiceDTO serviceDTO);

    List<ServiceDTO> getAllService();

    List<ServiceDTO> getServiceByStatut(StatutService statut);

    //modifier le statut d'une commande
    ServiceDTO modifierStatutService(Long serviceId, String nouveauStatut);

    ServiceDTO getServiceById(Long serviceId);

    // Ajouter la signature de la méthode de mise à jour
    int updateService(ServiceDTO serviceDTO);

    List<ServiceDTO> getServiceByCategorieId(Long categorie_id);

    List<ServiceDTO> getServiceByVilleId(Long ville_id);

    // ✅ Méthode de suppression à ajouter
    void deleteService(Long serviceId);
}
