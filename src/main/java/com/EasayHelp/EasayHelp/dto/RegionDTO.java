package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Villes;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegionDTO {

    private Long id;

    private String nom;

    private List<Villes> villes = new ArrayList<>();
}
