package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.CategorieDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Table(name="categories")
@ToString(exclude = {"services"})
@Data
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Lob
    @Column(name = "imageUrl", columnDefinition="LONGBLOB") // MySQL
    private byte[] imageUrl;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    public CategorieDTO getCategorieDTO(){
        CategorieDTO categorieDTO = new CategorieDTO();
        categorieDTO.setId(id);
        categorieDTO.setNom(nom);
        categorieDTO.setDescription(description);

        if (imageUrl != null && imageUrl.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageUrl);
            categorieDTO.setImageBase64(base64Image);  // il faut créer ce champ dans ton DTO
        }

        return categorieDTO;
    }
}
