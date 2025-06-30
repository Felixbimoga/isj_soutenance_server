package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.CategorieDTO;
import com.EasayHelp.EasayHelp.entity.Categorie;
import com.EasayHelp.EasayHelp.repository.CategorieRepository;
import com.EasayHelp.EasayHelp.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp") // Correction du nom ici aussi (typo : "easayHelph" → "easayHelp")
@RequiredArgsConstructor
public class CategorieController {

    @Autowired
    private CategorieService categorieService;
    @Autowired
    private CategorieRepository categorieRepository;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    //creation d'une categorie
    @PostMapping(value = "/create-categorie" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Categorie> createCategorie(@ModelAttribute  CategorieDTO categorieDTO){
        Categorie createCategorie = categorieService.createCategorie(categorieDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createCategorie);
    }

    //afficher la liste des categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategorieDTO>> getAllCategories(){
        List<CategorieDTO> allCategories = categorieService.getAllCategorie();
        return ResponseEntity.ok(allCategories);
    }

    // supprimer un commande
    @DeleteMapping("/categorie/supprimer/{id}")
    public ResponseEntity<?> supprimerCategorie(@PathVariable Long id) {
        try {
            categorieService.deleteCategorie(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getcategorie/{userId}")
    public ResponseEntity<List<CategorieDTO>> getCategoriesByUser(@PathVariable Long userId) {
        List<CategorieDTO> categoriesList = categorieService.getCategorieByUser(userId);
        return ResponseEntity.ok(categoriesList);
    }

    @PutMapping(value ="/categorie/updateCategories/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCatagorie(@PathVariable Long id, @ModelAttribute CategorieDTO categorieDTO, @RequestPart(value = "file", required = false) MultipartFile file) {
        categorieDTO.setId(id);
        try {
            int result = categorieService.updateCategorie(categorieDTO);
            if (result == 1) {
                return ResponseEntity.ok("Syllabus mis à jour avec succès");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour du syllabus");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //charger l'image
    @GetMapping("categorie/{id}/image")
    public ResponseEntity<byte[]> getCategorieImage(@PathVariable Long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie introuvable"));

        byte[] imageData = categorie.getImageUrl();

        if (imageData == null || imageData.length == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Aucune image trouvée pour cette catégorie");
        }

        // 🔁 Tu peux aussi détecter le format si tu stockes des images PNG/JPEG...
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // ou IMAGE_PNG si c’est ton format
        headers.setContentLength(imageData.length);

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
