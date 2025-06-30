package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.dto.FeedbackDTO;
import com.EasayHelp.EasayHelp.entity.Commande;
import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.repository.CommandeRepository;
import com.EasayHelp.EasayHelp.repository.FeedbackRepository;
import com.EasayHelp.EasayHelp.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final CommandeRepository commandeRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public Feedback createFeedback(FeedbackDTO feedbackDTO, Long commandeId) {
        Optional<Commande> optionalcommande = commandeRepository.findById(commandeId);

        if(optionalcommande.isPresent()){
            Feedback feedback = new Feedback();
            feedback.setCommentaire(feedbackDTO.getCommentaire());
            feedback.setNote(feedbackDTO.getNote());
            feedback.setDate_feedback(feedbackDTO.getDate_feedback());

            feedback.setCommande(optionalcommande.get());
            return feedbackRepository.save(feedback);
        }
        return null;
    }

    @Override
    public List<FeedbackDTO> getAllFeedback() {
        return feedbackRepository.findAll().stream()
                .map(Feedback::getFeedbackDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO getFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .map(Feedback::getFeedbackDTO)
                .orElse(null);
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback introuvable avec l'ID: " + id);
        }
        feedbackRepository.deleteById(id);
    }

    @Override
    public int updateFeedback(FeedbackDTO feedbackDTO) {
        return feedbackRepository.findById(feedbackDTO.getId()).map(feedback -> {
            feedback.setCommentaire(feedbackDTO.getCommentaire());
            feedback.setNote(feedbackDTO.getNote());
            feedback.setDate_feedback(feedbackDTO.getDate_feedback());
            //service.setActif(serviceDTO.isActif());
            //service.setImageUrl(serviceDTO.getImageUrl());
            feedbackRepository.save(feedback);
            return 1;
        }).orElse(0);
    }

    @Override
    public List<FeedbackDTO> getFeedbackByCommandeId(Long commande_id) {
        if(!commandeRepository.existsById(commande_id)){
            throw new IllegalArgumentException("L'ID n'existe pas");
        }
        List<Feedback> feedback =  feedbackRepository.findAllFeedbackByCommandeId(commande_id);
        return feedback.stream()
                .map(Feedback::getFeedbackDTO)
                .collect(Collectors.toList());
    }
}
