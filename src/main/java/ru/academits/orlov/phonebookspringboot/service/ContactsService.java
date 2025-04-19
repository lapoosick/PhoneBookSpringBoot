package ru.academits.orlov.phonebookspringboot.service;

import ru.academits.orlov.phonebookspringboot.entity.Contact;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;

import java.util.List;

public interface ContactsService {
    List<Contact> getContacts(String term);

    GeneralResponse saveContact(Contact contact);

    GeneralResponse deleteContact(int id);
}
