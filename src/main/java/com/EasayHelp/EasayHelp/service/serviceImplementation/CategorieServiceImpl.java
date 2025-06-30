package com.EasayHelp.EasayHelp.service.serviceImplementation;

import com.EasayHelp.EasayHelp.dto.CategorieDTO;
import com.EasayHelp.EasayHelp.entity.Categorie;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import com.EasayHelp.EasayHelp.repository.CategorieRepository;
import com.EasayHelp.EasayHelp.repository.UserRepository;
import com.EasayHelp.EasayHelp.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private final UserRepository userRepository;

    @Override
    public Categorie createCategorie(CategorieDTO categorieDTO) {
        if (categorieDTO.getUtilisateurId() == null) {
            throw new IllegalArgumentException("L'ID de l'utilisateur ne peut pas être nul");
        }
        // Vérifier si l'utilisateur existe
        Utilisateur utilisateur = userRepository.findById(categorieDTO.getUtilisateurId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Categorie categorie = new Categorie();

        categorie.setNom(categorieDTO.getNom());
        categorie.setDescription(categorieDTO.getDescription());

        // 🔁 Convertir l'image en byte[] depuis imageUrl (MultipartFile)
        try {
            if (categorieDTO.getImageUrl() != null && !categorieDTO.getImageUrl().isEmpty()) {
                categorie.setImageUrl(categorieDTO.getImageUrl().getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture de l'image", e);
        }

        // Associer l'utilisateur au categorie
        categorie.setUtilisateur(utilisateur);

        // Vérifier si les séances sont présentes
        if (categorieDTO.getServices() != null) {
            List<com.EasayHelp.EasayHelp.entity.Service> categories = new ArrayList<>();
            for (com.EasayHelp.EasayHelp.entity.Service seance : categorieDTO.getServices()) {
                seance.setCategorie(categorie);
                categories.add((com.EasayHelp.EasayHelp.entity.Service) seance);
            }
            categorie.setServices(categories);
        }

        return categorieRepository.save(categorie);
    }

    @Override
    public List<CategorieDTO> getAllCategorie() {
        return categorieRepository.findAll().stream()
                .map(Categorie::getCategorieDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategorie(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Service introuvable avec l'ID: " + id);
        }
        categorieRepository.deleteById(id);
    }

    @Override
    public List<CategorieDTO> getCategorieByUser(Long userId) {
        try {
            Optional<Utilisateur> existingUserOpt = userRepository.findById(userId);
            System.out.println(existingUserOpt);
//            if (!existingUserOpt.isPresent()) {
//                return;
//            }
//
            //Optional<Utilisateur> utilisateur = Optional.of(new Utilisateur());
            Utilisateur existingUser = existingUserOpt.get();

            List<Categorie> categorieList = categorieRepository.findAllByUtilisateurId(existingUser.getId());
            System.out.println("ssdsfdsfdsfdfsfd"+categorieList);
            return categorieList.stream()
                    .map(Categorie::getCategorieDTO)
                    .collect(Collectors.toList());

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return List.of();
    }

    @Override
    public int updateCategorie(CategorieDTO categorieDTO) {
        return categorieRepository.findById(categorieDTO.getId()).map(categorie -> {
            categorie.setNom(categorieDTO.getNom());
            categorie.setDescription(categorieDTO.getDescription());
            //categorie.setImageUrl(categorieDTO.getImageUrl());

            categorieRepository.save(categorie);
            return 1;
        }).orElse(0);
    }
}
