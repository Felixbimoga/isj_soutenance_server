package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.entity.Villes;

import java.util.List;
import java.util.Optional;

public interface VilleService {

    Villes save(Villes ville);

    Villes update(Long id, Villes ville);

    void delete(Long id);

    Villes getById(Long id);

    Optional<Villes> getByNom(String nom);

    List<Villes> getAll();

    boolean existsByNom(String nom);

    List<Villes> getVillesByRegionId(Long regionId);
}
