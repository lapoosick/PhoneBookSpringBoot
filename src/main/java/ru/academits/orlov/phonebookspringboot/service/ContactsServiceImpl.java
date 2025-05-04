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
        isEmptyString(contact.getSurname());
        isEmptyString(contact.getName());
        isEmptyString(contact.getPhoneNumber());

        if (contact.getId() == 0) {
            return contactsRepository.createContact(contact);
        }

        return contactsRepository.updateContact(contact);
    }

    @Override
    public GeneralResponse deleteContact(int id) {
        return contactsRepository.deleteContact(id);
    }

    private void isEmptyString(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Не заполнено обязательное поле.");
        }
    }
}
