package ru.academits.orlov.phonebookspringboot.service;

import org.springframework.stereotype.Service;
import ru.academits.orlov.phonebookspringboot.entity.Contact;
import ru.academits.orlov.phonebookspringboot.payload.GeneralResponse;
import ru.academits.orlov.phonebookspringboot.repository.ContactsRepository;

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
    public GeneralResponse saveContact(Contact contact) {
        if (contact.getSurname() == null || contact.getSurname().isEmpty()) {
            return new GeneralResponse(false, "Не указана фамилия.");
        }

        if (contact.getName() == null || contact.getName().isEmpty()) {
            return new GeneralResponse(false, "Не указано имя.");
        }

        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().isEmpty()) {
            return new GeneralResponse(false, "Не указан телефон.");
        }

        if (contact.getId() == 0) {
            return contactsRepository.saveContact(contact);
        }

        return contactsRepository.updateContact(contact);
    }

    @Override
    public GeneralResponse deleteContact(int id) {
        return contactsRepository.deleteContact(id);
    }
}
