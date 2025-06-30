package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.dto.ReclamationDTO;
import com.EasayHelp.EasayHelp.entity.Commande;
import com.EasayHelp.EasayHelp.entity.Reclamation;
import com.EasayHelp.EasayHelp.repository.CommandeRepository;
import com.EasayHelp.EasayHelp.repository.ReclamationRepository;
import com.EasayHelp.EasayHelp.service.ReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {

    private final CommandeRepository commandeRepository;
    private final ReclamationRepository reclamationRepository;

    @Override
    public Reclamation createReclamation(ReclamationDTO reclamationDTO, Long commandeId) {
        Optional<Commande> optionalcommande = commandeRepository.findById(commandeId);

        if(optionalcommande.isPresent()){
            Reclamation reclamation = new Reclamation();
            reclamation.setStatut(reclamationDTO.getStatut());
            reclamation.setDateReclamation(reclamationDTO.getDateReclamation());
            reclamation.setDescription(reclamationDTO.getDescription());

            reclamation.setCommande(optionalcommande.get());
            return reclamationRepository.save(reclamation);
        }
        return null;
    }

    @Override
    public List<ReclamationDTO> getAllReclamation() {
        return reclamationRepository.findAll().stream()
                .map(Reclamation::getReclamationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReclamationDTO getReclamationById(Long reclamationId) {
        return reclamationRepository.findById(reclamationId)
                .map(Reclamation::getReclamationDTO)
                .orElse(null);
    }

    @Override
    public void deleteReclamation(Long id) {
        if (!reclamationRepository.existsById(id)) {
            throw new RuntimeException("Feedback introuvable avec l'ID: " + id);
        }
        reclamationRepository.deleteById(id);
    }

    @Override
    public int updateReclamation(ReclamationDTO reclamationDTO) {
        return reclamationRepository.findById(reclamationDTO.getId()).map(feedback -> {
            feedback.setStatut(reclamationDTO.getStatut());
            feedback.setDescription(reclamationDTO.getDescription());
                feedback.setDateReclamation(reclamationDTO.getDateReclamation());

            reclamationRepository.save(feedback);
            return 1;
        }).orElse(0);
    }

    @Override
    public List<ReclamationDTO> getReclamationByCommandeId(Long commande_id) {
        if(!commandeRepository.existsById(commande_id)){
            throw new IllegalArgumentException("L'ID n'existe pas");
        }
        List<Reclamation> reclamation =  reclamationRepository.findAllReclamationByCommandeId(commande_id);
        return reclamation.stream()
                .map(Reclamation::getReclamationDTO)
                .collect(Collectors.toList());
    }

}
