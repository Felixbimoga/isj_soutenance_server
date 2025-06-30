package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.FeedbackDTO;
import com.EasayHelp.EasayHelp.dto.ReclamationDTO;
import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.entity.Reclamation;
import com.EasayHelp.EasayHelp.service.ReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp/reclamation")
@RequiredArgsConstructor
public class ReclamationController {

    private final ReclamationService reclamationService;

    // Créer un feedback
    @PostMapping("/{commandeId}/create-reclamation")
    public ResponseEntity<Reclamation> createReclamation(@RequestBody ReclamationDTO reclamationDTO, @PathVariable Long commandeId) {
        Reclamation createdReclamation = reclamationService.createReclamation(reclamationDTO,commandeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReclamation);
    }

    // Lister tous les reclamations
    @GetMapping("/reclamations")
    public ResponseEntity<List<ReclamationDTO>> getAllReclamation() {
        List<ReclamationDTO> allReclamation = reclamationService.getAllReclamation();
        return ResponseEntity.ok(allReclamation);
    }

    // supprimer un reclamation
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> supprimerReclamation(@PathVariable Long id) {
        try {
            reclamationService.deleteReclamation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Récupérer un reclamation par ID
    @GetMapping("/{reclamationId}")
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long reclamationId) {
        ReclamationDTO reclamation = reclamationService.getReclamationById(reclamationId);
        if (reclamation != null) {
            return ResponseEntity.ok(reclamation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{commandeId}/reclamations")
    public ResponseEntity<List<ReclamationDTO>> getFeedbackByCommandeId(@PathVariable Long commandeId){
        List<ReclamationDTO> reclamationList = reclamationService.getReclamationByCommandeId(commandeId);
        return ResponseEntity.ok(reclamationList);
    }
}
