package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Categorie;
import com.EasayHelp.EasayHelp.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {

    List<Categorie> findAllByUtilisateurId(Long utilisateurId);

    Optional<Categorie> findByNom(String nom);
}
