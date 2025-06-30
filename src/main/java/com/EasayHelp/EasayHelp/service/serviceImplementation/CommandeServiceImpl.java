package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.dto.CommandeDTO;
import com.EasayHelp.EasayHelp.entity.Commande;
import com.EasayHelp.EasayHelp.entity.Enum.Role;
import com.EasayHelp.EasayHelp.entity.Enum.StatutCommande;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import com.EasayHelp.EasayHelp.repository.CommandeRepository;
import com.EasayHelp.EasayHelp.repository.ServiceRepository;
import com.EasayHelp.EasayHelp.repository.UserRepository;
import com.EasayHelp.EasayHelp.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public Commande createCommande(CommandeDTO commandeDTO) {

        if (commandeDTO.getUtilisateurId() == null) {
            throw new IllegalArgumentException("L'ID de l'utilisateur ne peut pas être nul");
        }

        if (commandeDTO.getServiceId() == null) {
            throw new IllegalArgumentException("L'ID du servicer ne peut pas être nul");
        }
        // Vérifier si l'utilisateur existe
        Utilisateur utilisateur = userRepository.findById(commandeDTO.getUtilisateurId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Vérifier si le service existe
        com.EasayHelp.EasayHelp.entity.Service service = serviceRepository.findById(commandeDTO.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service introuvable"));

        Commande commande = new Commande();

        commande.setReference(commandeDTO.getReference());
        commande.setStatut(commandeDTO.getStatut());
        commande.setMontantTotal(commandeDTO.getMontantTotal());
        commande.setDateCommande(commandeDTO.getDateCommande());
        commande.setLocalisation(commandeDTO.getLocalisation());
        commande.setEcheance(commandeDTO.getEcheance());

        // Associer l'utilisateur à la commande
        commande.setUtilisateur(utilisateur);

        // Associer le service à la commande
        commande.setService(service);

        return commandeRepository.save(commande);
    }

    @Override
    public List<CommandeDTO> getAllCommande() {
        return commandeRepository.findAll().stream()
                .map(Commande::getCommandeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommande(Long id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("commande introuvable avec l'ID: " + id);
        }
        commandeRepository.deleteById(id);
    }

    @Override
    public List<CommandeDTO> getCommandeByUser(Long userId) {
        try {
            Optional<Utilisateur> existingUserOpt = userRepository.findById(userId);
            if (existingUserOpt.isEmpty()) {
                System.out.println("Utilisateur non trouvé avec ID : " + userId);
                return List.of();
            }

            Utilisateur existingUser = existingUserOpt.get();

            List<Commande> commandeList = commandeRepository.findByUtilisateurId(existingUser.getId());

            System.out.println("Commandes récupérées : " + commandeList.size());
            commandeList.forEach(cmd -> System.out.println("Commande ID : " + cmd.getId() + ", Référence : " + cmd.getReference()));

            return commandeList.stream()
                    .map(Commande::getCommandeDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<CommandeDTO> getCommandeByTechnicien(Long technicienId) {
        return userRepository.findById(technicienId)
                .map(technicien -> {
                    List<Commande> commandes = commandeRepository.findByTechnicienId(technicienId);

                    if (commandes.isEmpty()) {
                        System.out.println("Aucune commande trouvée pour le technicien ID : " + technicienId);
                    } else {
                        System.out.println("Commandes récupérées : " + commandes.size());
                        commandes.forEach(cmd ->
                                System.out.println("Commande ID : " + cmd.getId() + ", Référence : " + cmd.getReference()));
                    }

                    return commandes.stream()
                            .map(Commande::getCommandeDTO)
                            .collect(Collectors.toList());
                })
                .orElseGet(() -> {
                    System.out.println("Technicien introuvable avec l'ID : " + technicienId);
                    return List.of();
                });
    }

    @Override
    public CommandeDTO assignerCommandeAuTechnicien(Long commandeId, Long technicienId) {
        Optional<Commande> commandeOpt = commandeRepository.findById(commandeId);
        if (commandeOpt.isEmpty()) {
            throw new RuntimeException("Commande non trouvée avec ID : " + commandeId);
        }

        Optional<Utilisateur> technicienOpt = userRepository.findById(technicienId);
        if (technicienOpt.isEmpty()) {
            throw new RuntimeException("Technicien non trouvé avec ID : " + technicienId);
        }

        Utilisateur technicien = technicienOpt.get();
        // Optionnel : vérifier que c'est bien un technicien (si tu gères les rôles)
        if (technicien.getRole() != Role.TECHNICIEN) {
            throw new RuntimeException("L'utilisateur spécifié n'est pas un technicien.");
        }

        Commande commande = commandeOpt.get();
        commande.setTechnicien(technicien);
        Commande updated = commandeRepository.save(commande);

        return updated.getCommandeDTO(); // ou passe par un mapper si tu en as un
    }

    @Override
    public List<CommandeDTO> getCommandeByService(Long serviceId) {
        try {
            Optional<com.EasayHelp.EasayHelp.entity.Service> existingUserOpt = serviceRepository.findById(serviceId);
            System.out.println(existingUserOpt);

            com.EasayHelp.EasayHelp.entity.Service existingUser = existingUserOpt.get();

            List<Commande> commandeList = commandeRepository.findAllByServiceId(existingUser.getId());
            System.out.println("ssdsfdsfdsfdfsfd"+commandeList);
            return commandeList.stream()
                    .map(Commande::getCommandeDTO)
                    .collect(Collectors.toList());

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return List.of();
    }

    @Override
    public int updateCommande(CommandeDTO commandeDTO) {
        return commandeRepository.findById(commandeDTO.getId()).map(commande -> {

            commande.setReference(commandeDTO.getReference());
            commande.setStatut(commandeDTO.getStatut());
            commande.setMontantTotal(commandeDTO.getMontantTotal());
            commande.setDateCommande(commandeDTO.getDateCommande());
            commande.setLocalisation(commandeDTO.getLocalisation());
            commande.setEcheance(commandeDTO.getEcheance());

            commandeRepository.save(commande);
            return 1;
        }).orElse(0);
    }

    @Override
    public CommandeDTO getCommandeById(Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID : " + id));

        return mapToDTO(commande);
    }

    private CommandeDTO mapToDTO(Commande commande) {
        CommandeDTO dto = new CommandeDTO();
        dto.setId(commande.getId());
        dto.setDateCommande(commande.getDateCommande());
        dto.setStatut(commande.getStatut());

        // Mapping correct des IDs associés
        if (commande.getUtilisateur() != null) {
            dto.setUtilisateurId(commande.getUtilisateur().getId());
        }

        if (commande.getService() != null) {
            dto.setServiceId(commande.getService().getId());
        }

        return dto;
    }

    @Override
    public CommandeDTO modifierStatutCommande(Long commandeId, String nouveauStatut) {
        Optional<Commande> commandeOpt = commandeRepository.findById(commandeId);

        if (commandeOpt.isEmpty()) {
            throw new RuntimeException("Commande introuvable avec l'ID : " + commandeId);
        }

        Commande commande = commandeOpt.get();

        // Optionnel : Valider que le statut est autorisé
        List<String> statutsValides = List.of("EN_ATTENTE", "VALIDÉE", "EN_COURS", "TERMINÉE", "ANNULÉE");
        if (!statutsValides.contains(nouveauStatut.toUpperCase())) {
            throw new RuntimeException("Statut non valide : " + nouveauStatut);
        }

        commande.setStatut(StatutCommande.valueOf(nouveauStatut.toUpperCase()));

        Commande updated = commandeRepository.save(commande);
        return updated.getCommandeDTO();
    }
}
