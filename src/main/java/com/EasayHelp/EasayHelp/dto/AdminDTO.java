package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Enum.Role;
import lombok.Data;

@Data
public class AdminDTO {

    private long id;

    private String username;

    private String mail;

    private String password;

    private String sexe; // Exemples : "Masculin", "Féminin"

    private String telephone;

    private String photo; // Photo du profil (stockée sous forme de tableau d'octets)

    private Role role = Role.ADMIN;

}
