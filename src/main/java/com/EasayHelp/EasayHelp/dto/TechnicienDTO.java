package com.EasayHelp.EasayHelp.dto;

import com.EasayHelp.EasayHelp.entity.Feedback;
import com.EasayHelp.EasayHelp.entity.Enum.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TechnicienDTO {

    private Long id;

    private String username;

    private String mail;

    private String specialite;

    private String telephone;

    private String password;

    private String sexe;

    private String adress; //localisation

    private String photo;

    private String statut;

    private Long serviceId;

    private List<Feedback> feedback = new ArrayList<>();

    private Role role = Role.TECHNICIEN;
}
