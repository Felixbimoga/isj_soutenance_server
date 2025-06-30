package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.configuration.JwtUtils;
import com.EasayHelp.EasayHelp.dto.AdminDTO;
import com.EasayHelp.EasayHelp.dto.ClientDTO;
import com.EasayHelp.EasayHelp.dto.TechnicienDTO;
import com.EasayHelp.EasayHelp.entity.Enum.Role;
import com.EasayHelp.EasayHelp.entity.Utilisateur;
import com.EasayHelp.EasayHelp.repository.UserRepository;
import com.EasayHelp.EasayHelp.service.CustomUserDetailsService;
import com.EasayHelp.EasayHelp.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/easayHelp/auth")
@RequiredArgsConstructor
@Configuration
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationLoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final MailService mailService;

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminDTO dto) {
        if (userRepository.findByMail(dto.getMail()) != null) {
            return ResponseEntity.badRequest().body("L'utilisateur existe déjà");
        }

        Utilisateur admin = new Utilisateur();
        admin.setUsername(dto.getUsername());
        admin.setMail(dto.getMail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setSexe(dto.getSexe());
        admin.setTelephone(dto.getTelephone());
        // Traitement de la photo
        if (dto.getPhoto() != null && dto.getPhoto().contains(",")) {
            String base64Data = dto.getPhoto().split(",")[1];
            byte[] photoBytes = Base64.getDecoder().decode(base64Data);
            admin.setPhoto(photoBytes);
        } else {
            admin.setPhoto(null); // ou une photo par défaut
        }
        admin.setRole(Role.ADMIN);

        return ResponseEntity.ok(userRepository.save(admin));
    }

    @PostMapping(value = "/register/technicien", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerTechnicien(
            @RequestPart("technicien") String technicienJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile) {

        try {
            // Convertir le JSON en DTO
            ObjectMapper objectMapper = new ObjectMapper();
            TechnicienDTO dto = objectMapper.readValue(technicienJson, TechnicienDTO.class);

            // Vérification existence utilisateur
            if (userRepository.findByMail(dto.getMail()) != null) {
                return ResponseEntity.badRequest().body("L'utilisateur existe déjà");
            }

            // Création du technicien
            Utilisateur technicien = new Utilisateur();
            technicien.setUsername(dto.getUsername());
            technicien.setMail(dto.getMail());
            technicien.setPassword(passwordEncoder.encode(dto.getPassword()));
            technicien.setSexe(dto.getSexe());
            technicien.setTelephone(dto.getTelephone());
            technicien.setSpecialite(dto.getSpecialite());
            technicien.setAdress(dto.getAdress());
            technicien.setRole(Role.TECHNICIEN);

            // Traitement de la photo
            if (photoFile != null && !photoFile.isEmpty()) {
                technicien.setPhoto(photoFile.getBytes());
            } else if (dto.getPhoto() != null && dto.getPhoto().contains(",")) {
                // Fallback pour base64 si le fichier n'est pas envoyé via multipart
                String base64Data = dto.getPhoto().split(",")[1];
                byte[] photoBytes = Base64.getDecoder().decode(base64Data);
                technicien.setPhoto(photoBytes);
            } else {
                technicien.setPhoto(null);
            }

            // Enregistrement et envoi d'email
            Utilisateur savedTechnicien = userRepository.save(technicien);
            mailService.sendCredentialsToTechnician(dto.getMail(), dto.getUsername(), dto.getPassword());

            return ResponseEntity.ok(savedTechnicien);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Format JSON invalide");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur de traitement de l'image");
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@RequestBody ClientDTO dto) {
        if (userRepository.findByMail(dto.getMail()) != null) {
            return ResponseEntity.badRequest().body("L'utilisateur existe déjà");
        }

        Utilisateur client = new Utilisateur();
        client.setUsername(dto.getUsername());
        client.setMail(dto.getMail());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setSexe(dto.getSexe());
        client.setTelephone(dto.getTelephone());
        client.setAdress(dto.getAdress());

        // Traitement de la photo
        if (dto.getPhoto() != null && dto.getPhoto().contains(",")) {
            String base64Data = dto.getPhoto().split(",")[1];
            byte[] photoBytes = Base64.getDecoder().decode(base64Data);
            client.setPhoto(photoBytes);
        } else {
            client.setPhoto(null); // ou une photo par défaut
        }

        client.setRole(Role.USER);

        return ResponseEntity.ok(userRepository.save(client));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Utilisateur user) {
        try {
            log.info("Authenticating user: {}", user.getMail());

            // Récupérer l'utilisateur par mail
            Utilisateur utilisateur = userRepository.findByMail(user.getMail());

            // Vérifier s'il est activé (enabled)
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé");
            }
            if (!utilisateur.isEnabled()) {
                log.warn("Tentative de connexion d'un utilisateur désactivé: {}", utilisateur.getMail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Votre compte est désactivé. Veuillez contacter l’administrateur.");
            }

            // Maintenant que c'est validé, on fait l'authentification Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                log.info("User authenticated: {}, role: {}", utilisateur.getMail(), utilisateur.getRole());

                // Génération du token
                String token = jwtUtils.generateToken(authentication.getName(), authentication.getAuthorities().toString());

                // Création du DTO spécifique selon le rôle
                Object dto;
                switch (utilisateur.getRole()) {
                    case ADMIN:
                        dto = utilisateur.toAdminDTO();
                        break;
                    case TECHNICIEN:
                        dto = utilisateur.toTechnicienDTO();
                        break;
                    default:
                        dto = utilisateur.toClientDTO();
                        break;
                }

                Map<String, Object> authData = new HashMap<>();
                authData.put("token", token);
                authData.put("type", "Bearer");
                authData.put("role", utilisateur.getRole());
                authData.put("id", utilisateur.getId());
                authData.put("user", dto);

                return ResponseEntity.ok(authData);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom ou mot de passe invalide");

        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom ou mot de passe incorrect");
        }
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String sexe,
            @RequestParam(required = false) String specialite,
            @RequestParam(required = false) String adress,
            @RequestParam(required = false) MultipartFile photo) {

        try {
            Optional<Utilisateur> existingUserOpt = userRepository.findById(id);
            if (!existingUserOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }

            Utilisateur existingUser = existingUserOpt.get();

            // Mettre à jour les champs
            if (username != null) existingUser.setUsername(username);
            if (mail != null) existingUser.setMail(mail);
            if (telephone != null) existingUser.setTelephone(telephone);
            if (sexe != null) existingUser.setSexe(sexe);
            if (specialite != null) existingUser.setSpecialite(specialite);
            if (adress != null) existingUser.setAdress(adress);

            // Gérer la photo
            if (photo != null && !photo.isEmpty()) {
                existingUser.setPhoto(photo.getBytes());
            }

            userRepository.save(existingUser);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "Utilisateur mis à jour avec succès"));

        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Erreur lors de la mise à jour"));
        }
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public List<Utilisateur> rechercherAll() {
        return this.customUserDetailsService.findAll();
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<?> getUserPhoto(@PathVariable Long id) {
        try {
            byte[] photo = customUserDetailsService.getUserPhotoById(id);

            if (photo == null || photo.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune photo disponible pour cet utilisateur.");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // IMAGE_PNG si nécessaire
            headers.setContentLength(photo.length);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération de la photo: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Réinitialiser le cookie contenant le JWT (si utilisé)
        response.setHeader("Set-Cookie", "token=; Max-Age=0; Path=/; HttpOnly; SameSite=Strict;");

        // Réponse JSON avec un message de succès
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Déconnexion réussie");

        return ResponseEntity.ok(responseMap);
    }

    //Se deconnecter
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/logout")
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();  // Permet de répondre à la requête OPTIONS pour CORS
    }

    //Afficher détails de l'utilisateur
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        Utilisateur utilisateur = customUserDetailsService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateur); // <- pas de DTO ici
    }

    //Afficher le rôle
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Utilisateur>> getUsersByRole(@PathVariable String role) {
        List<Utilisateur> utilisateurs = customUserDetailsService.getUsersByRole(role);
        return ResponseEntity.ok(utilisateurs);
    }

    // Bloquer un utilisateur
    @PutMapping("/{id}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        try {
            customUserDetailsService.blockUser(id);
            return ResponseEntity.ok("Utilisateur bloqué avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Débloquer un utilisateur
    @PutMapping("/{id}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Long id) {
        try {
            customUserDetailsService.unblockUser(id);
            return ResponseEntity.ok("Utilisateur débloqué avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/techniciens/service/{id}")
    public List<Utilisateur> getTechsByService(@PathVariable Long id) {
        return customUserDetailsService.getTechniciensByService(id);
    }

}
