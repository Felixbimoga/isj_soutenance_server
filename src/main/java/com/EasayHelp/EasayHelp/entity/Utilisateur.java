package com.EasayHelp.EasayHelp.entity;

import com.EasayHelp.EasayHelp.dto.AdminDTO;
import com.EasayHelp.EasayHelp.dto.ClientDTO;
import com.EasayHelp.EasayHelp.dto.TechnicienDTO;
import com.EasayHelp.EasayHelp.entity.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name="Utilisateur")
@Data
@ToString(exclude = {"services"})
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;
    @Column(unique = true)
    private String mail;
    private String password;
    private String sexe; // Exemples : "Masculin", "Féminin"
    private String telephone;

    private String adress;

    private String statut; //Disponible, Indisponible

    private String specialite;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name="services_id",nullable = true)
    @JsonIgnore
    private Service service;

    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Commande> commandes;

    @OneToMany(mappedBy = "technicien", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Commande> commandesTechnicien;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo; // Photo du profil (stockée sous forme de tableau d'octets)

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    // ** Nouveau champ pour bloquer/débloquer l'utilisateur **
    @Column(nullable = false)
    private boolean enabled = true;  // true = actif, false = bloqué

    // Implémentation de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public ClientDTO toClientDTO() {
        ClientDTO dto = new ClientDTO();
        dto.setUsername(this.getUsername());
        dto.setMail(this.getMail());
        dto.setTelephone(this.getTelephone());
        dto.setSexe(this.getSexe());
        dto.setAdress(this.getAdress());

        if (this.getPhoto() != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(this.getPhoto());
            dto.setPhoto(photoBase64);
        } else {
            dto.setPhoto(null);
        }

        dto.setPassword(null); // par sécurité, on ne renvoie pas le mot de passe
        return dto;
    }

    public AdminDTO toAdminDTO() {
        AdminDTO dto = new AdminDTO();
        dto.setUsername(this.getUsername());
        dto.setMail(this.getMail());
        dto.setTelephone(this.getTelephone());
        dto.setSexe(this.getSexe());

        if (this.getPhoto() != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(this.getPhoto());
            dto.setPhoto(photoBase64);
        } else {
            dto.setPhoto(null);
        }

        dto.setPassword(null);
        return dto;
    }

    public TechnicienDTO toTechnicienDTO() {
        TechnicienDTO dto = new TechnicienDTO();
        dto.setUsername(this.getUsername());
        dto.setMail(this.getMail());
        dto.setTelephone(this.getTelephone());
        dto.setSexe(this.getSexe());

        if (this.getPhoto() != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(this.getPhoto());
            dto.setPhoto(photoBase64);
        } else {
            dto.setPhoto(null);
        }

        dto.setSpecialite(this.getSpecialite());
        dto.setPassword(null);
        return dto;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // On utilise le champ enabled pour bloquer l'utilisateur
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
