package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.dto.ServiceDTO;
import com.EasayHelp.EasayHelp.entity.Categorie;
import com.EasayHelp.EasayHelp.entity.Enum.StatutService;
import com.EasayHelp.EasayHelp.entity.Villes;
import com.EasayHelp.EasayHelp.repository.CategorieRepository;
import com.EasayHelp.EasayHelp.repository.ServiceRepository;
import com.EasayHelp.EasayHelp.repository.VilleRepository;
import com.EasayHelp.EasayHelp.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategorieRepository categorieRepository;
    private final VilleRepository villeRepository;

    @Override
    public com.EasayHelp.EasayHelp.entity.Service createService(ServiceDTO serviceDTO) {
        // 1. Trouver la catégorie par son nom
        Categorie categorie = categorieRepository.findByNom(serviceDTO.getCategorieNom())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec le nom: " + serviceDTO.getCategorieNom()));

        Villes ville = villeRepository.findByNom(serviceDTO.getVilleNom())
                .orElseThrow(() -> new RuntimeException("Ville non trouvée avec le nom: " + serviceDTO.getVilleNom()));

        // 2. Créer le service
        com.EasayHelp.EasayHelp.entity.Service service = new com.EasayHelp.EasayHelp.entity.Service();
        service.setNom(serviceDTO.getNom());
        service.setDescription(serviceDTO.getDescription());
        service.setPrix(serviceDTO.getPrix());
        service.setStatut(serviceDTO.getStatut());
        service.setCategorie(categorie);
        service.setVille(ville);

        // 🔁 Convertir l'image en byte[] depuis imageUrl (MultipartFile)
        try {
            if (serviceDTO.getImageUrl() != null && !serviceDTO.getImageUrl().isEmpty()) {
                service.setImageUrl(serviceDTO.getImageUrl().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture de l'image", e);
        }

        return serviceRepository.save(service);
    }

    @Override
    public List<ServiceDTO> getAllService() {
        return serviceRepository.findAll().stream()
                .map(com.EasayHelp.EasayHelp.entity.Service::getServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDTO getServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId)
                .map(com.EasayHelp.EasayHelp.entity.Service::getServiceDto)
                .orElse(null);
    }

    @Override
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service introuvable avec l'ID: " + id);
        }
        serviceRepository.deleteById(id);
    }

    @Override
    public int updateService(ServiceDTO serviceDTO) {
        return serviceRepository.findById(serviceDTO.getId()).map(service -> {
            service.setNom(serviceDTO.getNom());
            service.setDescription(serviceDTO.getDescription());
            service.setPrix(serviceDTO.getPrix());
            //service.setActif(serviceDTO.isActif());
            //service.setImageUrl(serviceDTO.getImageUrl());
            serviceRepository.save(service);
            return 1;
        }).orElse(0);
    }

    @Override
    public List<ServiceDTO> getServiceByCategorieId(Long categorie_id) {
        if(!categorieRepository.existsById(categorie_id)){
            throw new IllegalArgumentException("L'ID n'existe pas");
        }
        List<com.EasayHelp.EasayHelp.entity.Service> service =  serviceRepository.findAllServiceByCategorieId(categorie_id);
        return service.stream()
                .map(com.EasayHelp.EasayHelp.entity.Service::getServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> getServiceByVilleId(Long ville_id) {
        if(!villeRepository.existsById(ville_id)){
            throw new IllegalArgumentException("L'ID n'existe pas");
        }
        List<com.EasayHelp.EasayHelp.entity.Service> service =  serviceRepository.findAllServiceByVilleId(ville_id);
        return service.stream()
                .map(com.EasayHelp.EasayHelp.entity.Service::getServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> getServiceByStatut(StatutService statut) {
        List<com.EasayHelp.EasayHelp.entity.Service> services = serviceRepository.findByStatut(statut);

        return services.stream()
                .map(com.EasayHelp.EasayHelp.entity.Service::getServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDTO modifierStatutService(Long serviceId, String nouveauStatut) {
        Optional<com.EasayHelp.EasayHelp.entity.Service> serviceOpt = serviceRepository.findById(serviceId);

        if (serviceOpt.isEmpty()) {
            throw new RuntimeException("Service introuvable avec l'ID : " + serviceId);
        }

        com.EasayHelp.EasayHelp.entity.Service service = serviceOpt.get();

        // Optionnel : Valider que le statut est autorisé
        List<String> statutsValides = List.of("DISPONIBLE", "INDISPONIBLE");
        if (!statutsValides.contains(nouveauStatut.toUpperCase())) {
            throw new RuntimeException("Statut non valide : " + nouveauStatut);
        }

        service.setStatut(StatutService.valueOf(nouveauStatut.toUpperCase()));

        com.EasayHelp.EasayHelp.entity.Service updated = serviceRepository.save(service);
        return updated.getServiceDto();
    }
}
