package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Regions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Regions, Long> {

    /** Trouver une région par son nom (insensible à la casse) */
    Optional<Regions> findByNom(String nom);

    /** Vérifier si un nom est déjà pris (utile pour valider en front) */
    boolean existsByNom(String nom);

}
