package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.FeedbackDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String commentaire;
    private int note;
    private LocalDateTime date_feedback;

    // Relation avec la commande
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    // Méthode pour générer un DTO
    public FeedbackDTO getFeedbackDTO() {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(this.id);
        dto.setCommentaire(this.commentaire);
        dto.setNote(this.note);
        dto.setDate_feedback(this.date_feedback);

        return dto;
    }
}
