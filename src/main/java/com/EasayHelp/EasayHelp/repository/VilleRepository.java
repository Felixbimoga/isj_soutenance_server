package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Categorie;
import com.EasayHelp.EasayHelp.entity.Villes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VilleRepository extends JpaRepository<Villes, Long> {

    /** Toutes les villes d'une région donnée (clé étrangère) */
    List<Villes> findByRegionId(Long regionId);

    /** Même chose mais en passant l'entité  */
    // List<Ville> findByRegion(Region region);

    /** Rechercher une ville par son nom */
    Optional<Villes> findByNomIgnoreCase(String nom);

    Optional<Villes> findByNom(String nom);

    /** Unicité du nom */
    boolean existsByNomIgnoreCase(String nom);
}
