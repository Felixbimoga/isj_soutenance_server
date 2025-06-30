package com.EasayHelp.EasayHelp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="villes")
@Data
public class Villes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="region_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Regions region;

    @OneToMany(mappedBy = "ville", fetch = FetchType.LAZY)
    @JsonIgnore // Empêche la sérialisation automatique
    private List<Service> services = new ArrayList<>();

    // Transient fields (pour faciliter la manipulation côté front)
    @Transient
    public String getRegionNom() {
        return this.region != null ? this.region.getNom() : null;
    }

    @Transient
    public void setRegionNom(String nom) {
        if (this.region == null) {
            this.region = new Regions();
        }
        this.region.setNom(nom);
    }
}
