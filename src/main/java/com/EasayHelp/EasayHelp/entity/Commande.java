package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.CommandeDTO;
import com.EasayHelp.EasayHelp.entity.Enum.StatutCommande;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="commandes")
@ToString(exclude = {"Utilisateur"})
@Data
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @Column(nullable = false)
    private double montantTotal;
    private String localisation;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String  echeance; //date limite
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String  dateCommande;

    // Relation avec le client
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    // Relation avec le techniceins
    @ManyToOne
    @JoinColumn(name = "technicien_id", nullable = true)
    private Utilisateur technicien;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @OneToMany(mappedBy = "commande", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reclamation> reclamations = new ArrayList<>();

    @OneToMany(mappedBy = "commande", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();

    public CommandeDTO getCommandeDTO() {
        CommandeDTO commandeDTO = new CommandeDTO();
        commandeDTO.setId(this.id);
        commandeDTO.setReference(this.reference);
        commandeDTO.setStatut(this.statut);
        commandeDTO.setMontantTotal(this.montantTotal);
        commandeDTO.setDateCommande(this.dateCommande);
        commandeDTO.setLocalisation(this.localisation);
        commandeDTO.setEcheance(this.echeance);
        commandeDTO.setServiceId(this.service.getId());
        if (this.technicien != null) {
            commandeDTO.setTechnicienId(this.technicien.getId());
        }

        commandeDTO.setUtilisateurId(this.utilisateur.getId());

        return commandeDTO;
    }
}
