package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.ContactDTO;
import com.EasayHelp.EasayHelp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/easayHelp/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public Object sendMessage(@RequestBody ContactDTO dto) {
        return contactService.sendMessage(dto);
    }
}
