package ru.academits.orlov.phonebookspringboot.controller;

import org.springframework.web.bind.annotation.*;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;
import ru.academits.orlov.phonebookspringboot.entity.Contact;
import ru.academits.orlov.phonebookspringboot.service.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {
    private final ContactsService contactsService;

    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping
    public List<Contact> getContacts(@RequestParam(required = false) String term) {
        return contactsService.getContacts(term);
    }

    @PostMapping
    public GeneralResponse createOrUpdateContact(@RequestBody Contact contact) {
        return contactsService.createOrUpdateContact(contact);
    }

    @DeleteMapping("/{id}")
    public GeneralResponse deleteContact(@PathVariable int id) {
        return contactsService.deleteContact(id);
    }
}
