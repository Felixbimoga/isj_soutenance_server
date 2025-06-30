package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.entity.Reclamation;
import com.EasayHelp.EasayHelp.entity.Enum.StatutCommande;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommandeDTO {

    private Long id;

    private String reference;

    private StatutCommande statut; // Exemple : EN_ATTENTE, VALIDÉE, ANNULÉE

    private double montantTotal;

    private String  echeance;

    private String localisation;

    private String  dateCommande;

    //@JsonProperty("utilisateurId")
    private Long utilisateurId;

    //@JsonProperty("utilisateurId")
    private Long technicienId;

    @JsonProperty("serviceId")
    private Long serviceId;

    private List<Reclamation> reclamations = new ArrayList<>();

    private List<Feedback> feedbacks = new ArrayList<>();
}
