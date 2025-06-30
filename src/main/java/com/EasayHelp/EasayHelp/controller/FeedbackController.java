package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.FeedbackDTO;
import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // Créer un feedback
    @PostMapping("/{commandeId}/create-feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackDTO feedbackDTO, @PathVariable Long commandeId) {
        Feedback createdFeedback = feedbackService.createFeedback(feedbackDTO,commandeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedback);
    }

    // Lister tous les feedbacks
    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<FeedbackDTO> allFeedback = feedbackService.getAllFeedback();
        return ResponseEntity.ok(allFeedback);
    }

    // supprimer un feedback
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<?> supprimerFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Récupérer un feedback par ID
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long feedbackId) {
        FeedbackDTO feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{commandeId}/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByCommandeId(@PathVariable Long commandeId){
        List<FeedbackDTO> feedbackList = feedbackService.getFeedbackByCommandeId(commandeId);
        return ResponseEntity.ok(feedbackList);
    }
}
