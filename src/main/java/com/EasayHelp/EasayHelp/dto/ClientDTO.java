package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Commande;
import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.entity.Enum.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClientDTO {

    private long id;

    private String username;

    private String mail;

    private String password;

    private String sexe; // Exemples : "Masculin", "Féminin"

    private String telephone;

    private String adress; //localisation

    private String photo; // Photo du profil (stockée sous forme de tableau d'octets)

    private Role role = Role.USER;

    private List<Commande> commandes = new ArrayList<>();

    private List<Feedback> feedback = new ArrayList<>();
}
