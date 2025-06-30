package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.ReclamationDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reclamations")
@Data
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, TRAITÉE, REJETÉE
    private LocalDateTime dateReclamation;

    // Relation avec la commande
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    // Méthode pour transformer en DTO
    public ReclamationDTO getReclamationDTO() {
        ReclamationDTO dto = new ReclamationDTO();
        dto.setId(this.id);
        dto.setStatut(this.statut);
        dto.setDescription(this.description);
        dto.setDateReclamation(this.dateReclamation);

        return dto;
    }
}
