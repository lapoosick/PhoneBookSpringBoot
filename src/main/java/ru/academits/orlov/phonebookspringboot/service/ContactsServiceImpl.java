package ru.academits.orlov.phonebookspringboot.service;

import org.springframework.stereotype.Service;
import ru.academits.orlov.phonebookspringboot.dao.ContactsRepository;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;
import ru.academits.orlov.phonebookspringboot.entity.Contact;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;

    public ContactsServiceImpl(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @Override
    public List<Contact> getContacts(String term) {
        return contactsRepository.getContacts(term);
    }

    @Override
    public GeneralResponse createOrUpdateContact(Contact contact) {
        try {
            contact.setSurname(trimString(contact.getSurname()));
            contact.setName(trimString(contact.getName()));
            contact.setPhoneNumber(trimString(contact.getPhoneNumber()));
        } catch (IllegalArgumentException e) {
            return GeneralResponse.getErrorResponse(e.getMessage());
        }

        if (contact.getId() == 0) {
            return contactsRepository.createContact(contact);
        }

        return contactsRepository.updateContact(contact);
    }

    @Override
    public GeneralResponse deleteContact(int id) {
        return contactsRepository.deleteContact(id);
    }

    private static String trimString(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Не заполнено обязательное поле.");
        }

        return string.trim();
    }
}
