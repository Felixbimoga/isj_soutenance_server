package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.CommandeDTO;
import com.EasayHelp.EasayHelp.entity.Commande;
import com.EasayHelp.EasayHelp.repository.CommandeRepository;
import com.EasayHelp.EasayHelp.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp/commande")
@RequiredArgsConstructor
public class CommandeController {

    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    //creation d'une commande
    @PostMapping(value = "/create-commande", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Commande> createCategorie(@ModelAttribute CommandeDTO commandeDTO){

        // Vérifier que commandeDTO contient bien l'ID utilisateur
        if (commandeDTO.getUtilisateurId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Commande createCommande = commandeService.createCommande(commandeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createCommande);
    }

    //afficher la liste des commandes
    @GetMapping("/commandes")
    public ResponseEntity<List<CommandeDTO>> getAllCommandes(){
        List<CommandeDTO> allCommandes = commandeService.getAllCommande();
        return ResponseEntity.ok(allCommandes);
    }

    //afficher la liste des commandes
    @GetMapping("/commandes/{id}")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id){
        CommandeDTO commande = commandeService.getCommandeById(id);
        return ResponseEntity.ok(commande);
    }

    // supprimer une commande
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> supprimerCommande(@PathVariable Long id) {
        try {
            commandeService.deleteCommande(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getcommandes/user/{userId}")
    public ResponseEntity<List<CommandeDTO>> getCommandeByUser(@PathVariable Long userId) {
        List<CommandeDTO> commandeList = commandeService.getCommandeByUser(userId);
        return ResponseEntity.ok(commandeList);
    }

    @GetMapping("/getcommandes/technicien/{technicienId}")
    public ResponseEntity<List<CommandeDTO>> getCommandeByTechnicien(@PathVariable Long technicienId) {
        List<CommandeDTO> commandeList = commandeService.getCommandeByTechnicien(technicienId);
        return ResponseEntity.ok(commandeList);
    }

    @PutMapping("/{commandeId}/assigner/{technicienId}")
    public ResponseEntity<CommandeDTO> assignerCommande(@PathVariable Long commandeId, @PathVariable Long technicienId ) {
        CommandeDTO result = commandeService.assignerCommandeAuTechnicien(commandeId, technicienId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getcommandes/service/{serviceId}")
    public ResponseEntity<List<CommandeDTO>> getCommandeByService(@PathVariable Long serviceId) {
        List<CommandeDTO> commandeList = commandeService.getCommandeByService(serviceId);
        return ResponseEntity.ok(commandeList);
    }

    @PutMapping("/updateCommande/{commandeId}")
    public ResponseEntity<?> updateCommande(@PathVariable Long commandeId, @RequestBody CommandeDTO commandeDTO) {
        commandeDTO.setId(commandeId);
        try {
            int result = commandeService.updateCommande(commandeDTO);
            if (result == 1) {
                return ResponseEntity.ok("Commande mise à jour avec succès");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour du syllabus");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{commandeId}/statut")
    public ResponseEntity<CommandeDTO> modifierStatutCommande(@PathVariable Long commandeId, @RequestParam String statut ) {
        CommandeDTO result = commandeService.modifierStatutCommande(commandeId, statut);
        return ResponseEntity.ok(result);
    }

}
