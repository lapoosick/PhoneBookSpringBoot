package ru.academits.orlov.phonebookspringboot.dao;

import ru.academits.orlov.phonebookspringboot.entity.Contact;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;

import java.util.List;

public interface ContactsRepository {
    List<Contact> getContacts(String term);

    GeneralResponse saveContact(Contact contact);

    GeneralResponse updateContact(Contact contact);

    GeneralResponse deleteContact(int id);
}
