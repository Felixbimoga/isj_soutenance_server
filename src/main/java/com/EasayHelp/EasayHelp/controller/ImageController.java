package com.EasayHelp.EasayHelp.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final Path uploadDir = Paths.get("uploads");

    public ImageController() throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir); // Crée le dossier uploads si pas existant
        }
    }

    // Endpoint pour uploader une image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }
            // Nettoyage du nom de fichier
            String filename = Path.of(file.getOriginalFilename()).getFileName().toString();

            // Sauvegarde dans uploads/
            Path destination = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Fichier uploadé avec succès: " + filename);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'upload");
        }
    }

    // Endpoint pour récupérer une image par son nom
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(file);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
