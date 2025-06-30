package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.ServiceDTO;
import com.EasayHelp.EasayHelp.entity.Service;
import com.EasayHelp.EasayHelp.entity.Enum.StatutService;
import com.EasayHelp.EasayHelp.repository.ServiceRepository;
import com.EasayHelp.EasayHelp.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp") // Correction du nom ici aussi (typo : "easayHelph" → "easayHelp")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    @Autowired
    private ServiceRepository serviceRepository;

    @PostMapping("/service/create-service")
    public ResponseEntity<Service> createService(@ModelAttribute ServiceDTO serviceDTO) {
        try {
            // 1. Validation des données
            if (serviceDTO.getCategorieNom() == null || serviceDTO.getCategorieNom().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            if (serviceDTO.getVilleNom() == null || serviceDTO.getVilleNom().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // 2. Création du service
            Service createdService = serviceService.createService(serviceDTO);

            // 3. Retourner la réponse
            return ResponseEntity.status(HttpStatus.CREATED).body(createdService);

        } catch (RuntimeException e) {
            // Gérer les erreurs spécifiques
            if (e.getMessage().contains("Catégorie non trouvée")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Lister tous les services
    @GetMapping("/services")
    public ResponseEntity<List<ServiceDTO>> getAllService() {
        List<ServiceDTO> allService = serviceService.getAllService();
        return ResponseEntity.ok(allService);
    }

    // supprimer un service
    @DeleteMapping("/service/supprimer/{id}")
    public ResponseEntity<?> supprimerService(@PathVariable Long id) {
        try {
            serviceService.deleteService(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Récupérer un service par ID
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long serviceId) {
        ServiceDTO service = serviceService.getServiceById(serviceId);
        if (service != null) {
            return ResponseEntity.ok(service);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categorie/{categorieId}/services")
    public ResponseEntity<List<ServiceDTO>> getServiceByCategorieId(@PathVariable Long categorieId){
        List<ServiceDTO> serviceList = serviceService.getServiceByCategorieId(categorieId);
        return ResponseEntity.ok(serviceList);
    }

    @GetMapping("/ville/{villeId}/services")
    public ResponseEntity<List<ServiceDTO>> getServiceByVilleId(@PathVariable Long villeId){
        List<ServiceDTO> serviceList = serviceService.getServiceByVilleId(villeId);
        return ResponseEntity.ok(serviceList);
    }

    //charger l'image
    @GetMapping("service/{id}/image")
    public ResponseEntity<byte[]> getServiceImage(@PathVariable Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable"));

        byte[] imageData = service.getImageUrl();

        if (imageData == null || imageData.length == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Aucune image trouvée pour cette catégorie");
        }

        // 🔁 Tu peux aussi détecter le format si tu stockes des images PNG/JPEG...
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // ou IMAGE_PNG si c’est ton format
        headers.setContentLength(imageData.length);

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    //recuperer les services par statut
    @GetMapping("/service/statut/{statut}")
    public ResponseEntity<List<ServiceDTO>> getByStatut(@PathVariable StatutService statut) {
        List<ServiceDTO> dtoList = serviceService.getServiceByStatut(statut);
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/service/{serviceId}/statut")
    public ResponseEntity<ServiceDTO> modifierStatutService(@PathVariable Long serviceId, @RequestParam String statut ) {
        ServiceDTO result = serviceService.modifierStatutService(serviceId, statut);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value ="/service/updateServices/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateService(@PathVariable Long id, @ModelAttribute ServiceDTO serviceDTO, @RequestPart(value = "file", required = false) MultipartFile file) {
        serviceDTO.setId(id);
        try {
            int result = serviceService.updateService(serviceDTO);
            if (result == 1) {
                return ResponseEntity.ok("Service mis à jour avec succès");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour du service");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
