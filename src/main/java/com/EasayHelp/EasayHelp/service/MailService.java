package com.EasayHelp.EasayHelp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendCredentialsToTechnician(String toEmail, String mail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("atanganalorel@gmail.com");
        message.setSubject("Bienvenue sur EasayHelp");

        String text = "Bonjour " + mail + ",\n\n" +
                "Bienvenue sur EasayHelp !\n\n" +
                "Votre compte technicien a été créé avec succès. Voici vos informations de connexion :\n\n" +
                "Identifiant : " + mail + "\n" +
                "code temporaire : " + password + "\n\n" +
                "Nous vous conseillons de le changer dès votre première connexion.\n\n" +
                "Accédez à votre espace via la plateforme EasayHelp.\n\n" +
                "Bien cordialement,\n" +
                "L’équipe EasayHelp";

        message.setText(text);
        mailSender.send(message);
    }

    public void sendContactMessage(String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo("atanganalorel@gmail.com");
        message.setSubject("Nouveau message depuis l'application");
        message.setText("Contenu du message :\n\n" + messageContent);

        mailSender.send(message);
    }
}
