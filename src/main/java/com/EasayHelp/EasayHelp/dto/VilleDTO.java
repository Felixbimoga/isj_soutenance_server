package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Service;
import com.EasayHelp.EasayHelp.entity.Villes;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VilleDTO {

    private Long id;

    private String nom;

    private Long regionId;

    private String regionNom;

    private List<Service> services = new ArrayList<>();
}
