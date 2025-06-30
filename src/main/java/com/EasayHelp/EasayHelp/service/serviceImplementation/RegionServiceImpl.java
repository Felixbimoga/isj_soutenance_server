package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.dto.RegionDTO;
import com.EasayHelp.EasayHelp.entity.Regions;
import com.EasayHelp.EasayHelp.repository.RegionRepository;
import com.EasayHelp.EasayHelp.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public Regions save(Regions region) {
        return regionRepository.save(region);
    }

    @Override
    public Optional<Regions> findById(Long id) {      // NEW
        return regionRepository.findById(id);
    }

    @Override
    public Regions update(Long id, Regions region) {
        Regions existing = getById(id);
        existing.setNom(region.getNom());
        return regionRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        regionRepository.deleteById(id);
    }

    @Override
    public Regions getById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'id : " + id));
    }

    @Override
    public Optional<Regions> getByNom(String nom) {
        return regionRepository.findByNom(nom);
    }

    @Override
    public List<RegionDTO> getAll() {
        // Crée une nouvelle liste totalement indépendante
        List<Regions> entities = new ArrayList<>();
        regionRepository.findAll().forEach(entities::add); // Copie élément par élément

        return entities.stream()
                .map(Regions::getRegionDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNom(String nom) {
        return regionRepository.existsByNom(nom);
    }
}