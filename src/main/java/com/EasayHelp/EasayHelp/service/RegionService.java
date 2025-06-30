package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.RegionDTO;
import com.EasayHelp.EasayHelp.entity.Regions;

import java.util.List;
import java.util.Optional;

public interface RegionService {

    Regions save(Regions region);

    Regions update(Long id, Regions region);

    void delete(Long id);

    Regions getById(Long id);

    Optional<Regions> getByNom(String nom);

    List<RegionDTO> getAll();

    Optional<Regions> findById(Long id);

    boolean existsByNom(String nom);
}
