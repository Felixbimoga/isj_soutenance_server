package com.EasayHelp.EasayHelp.initializer;

import com.EasayHelp.EasayHelp.entity.Regions;
import com.EasayHelp.EasayHelp.repository.RegionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRegionInitializer {

    private final RegionRepository regionRepository;

    @PostConstruct
    public void initializeRegions() {
        List<String> nomsRegions = Arrays.asList(
                "Adamaoua", "Centre", "Est", "Extrême-Nord", "Littoral",
                "Nord", "Nord-Ouest", "Ouest", "Sud", "Sud-Ouest"
        );

        for (String nom : nomsRegions) {
            if (!regionRepository.existsByNom(nom)) {
                Regions region = new Regions();
                region.setNom(nom);
                regionRepository.save(region);
            }
        }
    }
}