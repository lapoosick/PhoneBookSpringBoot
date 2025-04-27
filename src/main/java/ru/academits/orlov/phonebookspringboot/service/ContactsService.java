package ru.academits.orlov.phonebookspringboot.service;

import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;
import ru.academits.orlov.phonebookspringboot.entity.Contact;

import java.util.List;

public interface ContactsService {
    List<Contact> getContacts(String term);

    GeneralResponse createContact(Contact contact);

    GeneralResponse deleteContact(int id);
}
