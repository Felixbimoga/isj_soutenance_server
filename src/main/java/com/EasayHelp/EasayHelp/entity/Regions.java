package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.RegionDTO;
import com.EasayHelp.EasayHelp.dto.ServiceDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="regions")
@Data
public class Regions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    @JsonIgnore // Empêche la sérialisation automatique
    private List<Villes> villes = new ArrayList<>();

    public RegionDTO getRegionDto(){
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(id);
        regionDTO.setNom(nom);

        return regionDTO;
    }
}
