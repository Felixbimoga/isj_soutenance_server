package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.entity.Commande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CommandeWebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Méthode appelée quand une commande est passée (ex: depuis ton API REST existante)
    public void notifyNewCommande(Commande commande) {
        messagingTemplate.convertAndSend("/topic/new-commandes", commande);
    }
}
