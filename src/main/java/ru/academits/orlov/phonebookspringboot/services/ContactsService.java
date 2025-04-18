package ru.academits.orlov.phonebookspringboot.services;

import ru.academits.orlov.phonebookspringboot.entities.Contact;
import ru.academits.orlov.phonebookspringboot.payload.GeneralResponse;

import java.util.List;

public interface ContactsService {
    List<Contact> getContacts(String term);

    GeneralResponse saveContact(Contact contact);

    GeneralResponse deleteContact(int id);
}
