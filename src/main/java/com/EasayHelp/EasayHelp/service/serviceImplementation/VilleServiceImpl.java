package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.entity.Villes;
import com.EasayHelp.EasayHelp.repository.VilleRepository;
import com.EasayHelp.EasayHelp.service.VilleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VilleServiceImpl implements VilleService {

    private final VilleRepository villeRepository;

    @Override
    public Villes save(Villes ville) {
        return villeRepository.save(ville);
    }

    @Override
    public Villes update(Long id, Villes ville) {
        Villes existing = getById(id);
        existing.setNom(ville.getNom());
        existing.setRegion(ville.getRegion());
        return villeRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        villeRepository.deleteById(id);
    }

    @Override
    public Villes getById(Long id) {
        return villeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ville non trouvée avec l'id : " + id));
    }

    @Override
    public Optional<Villes> getByNom(String nom) {
        return villeRepository.findByNomIgnoreCase(nom);
    }

    @Override
    public List<Villes> getAll() {
        return villeRepository.findAll();
    }

    @Override
    public boolean existsByNom(String nom) {
        return villeRepository.existsByNomIgnoreCase(nom);
    }

    @Override
    public List<Villes> getVillesByRegionId(Long regionId) {
        return villeRepository.findByRegionId(regionId);
    }
}
