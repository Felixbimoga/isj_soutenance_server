package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Commande;
import com.EasayHelp.EasayHelp.entity.Enum.StatutService;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceDTO {

    private Long id;

    private String nom;

    private String description;

    private int prix;

    private StatutService statut;

    private MultipartFile imageUrl;

    // Pour envoyer l'image en base64 (GET)
    private String imageBase64;

    private Long categorieId;

    private Long villeId;

    private String categorieNom;

    private String villeNom;

    private List<Utilisateur> techniciens = new ArrayList<>();

    private List<Commande> commandes = new ArrayList<>();
}
