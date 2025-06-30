package com.EasayHelp.EasayHelp.service;

import com.EasayHelp.EasayHelp.dto.ContactDTO;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ContactService {

    private final MailService emailService;

    public ContactService(MailService emailService) {
        this.emailService = emailService;
    }

    public ResponseEntity<Map<String, String>> sendMessage(ContactDTO dto) {
        if (dto.getMessage() == null || dto.getMessage().trim().isEmpty()) {
            return createResponse("error", "Le message ne peut pas être vide", HttpStatus.BAD_REQUEST);
        }

        try {
            emailService.sendContactMessage(dto.getMessage());
            return createResponse("success", "Votre message a été envoyé avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse("error", "Erreur lors de l'envoi du message : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, String>> createResponse(String status, String message, HttpStatus httpStatus) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
