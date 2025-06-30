package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.CategorieDTO;
import com.EasayHelp.EasayHelp.entity.Categorie;

import java.util.List;

public interface CategorieService {

    Categorie createCategorie(CategorieDTO categorieDTO);

    List<CategorieDTO> getAllCategorie();

    List<CategorieDTO> getCategorieByUser(Long userId);

    // Ajouter la signature de la méthode de mise à jour
    int updateCategorie(CategorieDTO categorieDTO);

    void deleteCategorie(Long categorieId);
}
