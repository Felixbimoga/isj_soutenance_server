package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.ServiceDTO;
import com.EasayHelp.EasayHelp.entity.Enum.StatutService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Base64;
import java.util.Set;

@Entity
@Table(name="services")
@Data
@ToString(exclude = {"categorie"})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int prixMin;

    @Column(nullable = false)
    private int prixMax;

    @Lob
    @Column(name = "imageUrl", columnDefinition="LONGBLOB") // MySQL
    private byte[] imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutService statut;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Commande> commandes;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="categorie_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="ville_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Villes ville;

    @Transient  // Cette annotation indique que ce champ n'est pas persisté en base
    public String getCategorieNom() {
        return this.categorie != null ? this.categorie.getNom() : null;
    }

    @Transient  // Cette annotation indique que ce champ n'est pas persisté en base
    public String getVilleNom() {
        return this.ville != null ? this.ville.getNom() : null;
    }

    @Transient
    public void setCategorieNom(String nom) {
        if (this.categorie == null) {
            this.categorie = new Categorie();
        }
        this.categorie.setNom(nom);
    }

    @Transient
    public void setVilleNom(String nom) {
        if (this.ville == null) {
            this.ville = new Villes();
        }
        this.ville.setNom(nom);
    }

    public ServiceDTO getServiceDto(){
        ServiceDTO serviceDto = new ServiceDTO();
        serviceDto.setId(id);
        serviceDto.setNom(nom);
        serviceDto.setDescription(description);
        serviceDto.setPrixMax(prixMax);
        serviceDto.setPrixMin(prixMin);
        serviceDto.setStatut(statut);
        serviceDto.setVilleNom(this.getVilleNom());
        serviceDto.setCategorieNom(this.getCategorieNom());

        if (imageUrl != null && imageUrl.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageUrl);
            serviceDto.setImageBase64(base64Image);  // il faut créer ce champ dans ton DTO
        }

        serviceDto.setCategorieId(this.categorie.getId());
        serviceDto.setVilleId(this.ville.getId());
        return serviceDto;
    }
}
