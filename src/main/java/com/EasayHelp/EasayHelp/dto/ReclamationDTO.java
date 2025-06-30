package com.EasayHelp.EasayHelp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReclamationDTO {

    private Long id;

    private String description;

    private String statut;

    private LocalDateTime dateReclamation;

    private Long commandeId;
}
