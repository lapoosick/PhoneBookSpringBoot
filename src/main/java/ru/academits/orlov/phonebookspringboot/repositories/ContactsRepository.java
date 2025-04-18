package ru.academits.orlov.phonebookspringboot.repositories;

import ru.academits.orlov.phonebookspringboot.entities.Contact;
import ru.academits.orlov.phonebookspringboot.payload.GeneralResponse;

import java.util.List;

public interface ContactsRepository {
    List<Contact> getContacts(String term);

    GeneralResponse saveContact(Contact contact);

    GeneralResponse updateContact(Contact contact);

    GeneralResponse deleteContact(int id);
}
