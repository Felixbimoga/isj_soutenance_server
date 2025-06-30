package com.EasayHelp.EasayHelp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {

    private Long id;

    private String commentaire;

    private int note;

    private LocalDateTime date_feedback;

    private Long commandeId;
}
