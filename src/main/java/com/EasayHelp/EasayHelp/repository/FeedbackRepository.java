package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    List<Feedback> findAllById(Long id);

    List<Feedback> findAllByCommandeId(Long id);

    List<Feedback> findAllFeedbackByCommandeId(Long commande_id);
}
