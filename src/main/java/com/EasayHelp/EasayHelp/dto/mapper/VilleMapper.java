package com.EasayHelp.EasayHelp.dto.mapper;

import com.EasayHelp.EasayHelp.dto.VilleDTO;
import com.EasayHelp.EasayHelp.entity.Villes;
import org.springframework.stereotype.Component;

@Component          // auto‑détecté par Spring, prêt à être injecté
public class VilleMapper {

    public VilleDTO toDTO(Villes entity) {
        VilleDTO dto = new VilleDTO();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setRegionId(entity.getRegion().getId());
        dto.setRegionNom(entity.getRegion().getNom());
        return dto;
    }

    public Villes toEntity(VilleDTO dto) {
        Villes ville = new Villes();
        ville.setNom(dto.getNom());
        // la région sera injectée côté service → ville.setRegion(region);
        return ville;
    }
}
