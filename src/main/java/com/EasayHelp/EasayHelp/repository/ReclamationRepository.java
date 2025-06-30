package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {

    List<Reclamation> findAllById(Long id);

    List<Reclamation> findAllByCommandeId(Long id);

    List<Reclamation> findAllReclamationByCommandeId(Long commande_id);
}
