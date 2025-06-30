package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.FeedbackDTO;
import com.EasayHelp.EasayHelp.dto.ServiceDTO;
import com.EasayHelp.EasayHelp.entity.Feedback;

import java.util.List;

public interface FeedbackService {

    Feedback createFeedback(FeedbackDTO feedbackDTO, Long commande_id);

    List<FeedbackDTO> getAllFeedback();

    FeedbackDTO getFeedbackById(Long feedbackId);

    // Ajouter la signature de la méthode de mise à jour
    int updateFeedback(FeedbackDTO feedbackDTO);

    List<FeedbackDTO> getFeedbackByCommandeId(Long commande_id);

    void deleteFeedback(Long feedbackDTO);
}
