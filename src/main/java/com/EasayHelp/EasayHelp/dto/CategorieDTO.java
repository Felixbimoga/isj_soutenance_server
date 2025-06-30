package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Service;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategorieDTO {

    private Long id;

    private String nom;

    private String description;
    private MultipartFile imageUrl;

    // Pour envoyer l'image en base64 (GET)
    private String imageBase64;

    private List<Service> services = new ArrayList<>();
    //implementation de la relation entre categorie et service

    private Long utilisateurId;
}
