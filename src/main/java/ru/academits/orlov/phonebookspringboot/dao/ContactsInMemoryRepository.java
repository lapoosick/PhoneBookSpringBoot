package ru.academits.orlov.phonebookspringboot.dao;

import org.springframework.stereotype.Repository;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;
import ru.academits.orlov.phonebookspringboot.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ContactsInMemoryRepository implements ContactsRepository {
    private static final List<Contact> contacts = new ArrayList<>();
    private static final AtomicInteger newId = new AtomicInteger();

    @Override
    public List<Contact> getContacts(String term) {
        synchronized (contacts) {
            if (term == null || term.isEmpty()) {
                return contacts.stream()
                        .map(Contact::new)
                        .toList();
            }

            String termLowerCase = term.toLowerCase();

            return contacts.stream()
                    .filter(contact -> contact.getSurname().toLowerCase().contains(termLowerCase)
                            || contact.getName().toLowerCase().contains(termLowerCase)
                            || contact.getPhoneNumber().toLowerCase().contains(termLowerCase))
                    .toList();
        }
    }

    @Override
    public GeneralResponse createContact(Contact contact) {
        synchronized (contacts) {
            String contactPhoneNumber = contact.getPhoneNumber();

            if (contacts.stream().anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                return GeneralResponse.getErrorResponse("Контакт с номером телефона " + contactPhoneNumber + " уже существует.");
            }

            contact.setId(newId.incrementAndGet());

            contacts.add(contact);

            return GeneralResponse.getSuccessResponse();
        }
    }

    @Override
    public GeneralResponse updateContact(Contact contact) {
        synchronized (contacts) {
            int contactId = contact.getId();

            Contact repositoryContact = contacts.stream()
                    .filter(c -> c.getId() == contactId).findFirst().orElse(null);

            if (repositoryContact == null) {
                return GeneralResponse.getErrorResponse("Контакт с id = " + contactId + " не найден.");
            }

            String contactPhoneNumber = contact.getPhoneNumber();

            if (!contactPhoneNumber.equalsIgnoreCase(repositoryContact.getPhoneNumber())
                    && contacts.stream().anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                return GeneralResponse.getErrorResponse("Контакт с номером телефона " + contactPhoneNumber + " уже существует.");
            }

            repositoryContact.setSurname(contact.getSurname());
            repositoryContact.setName(contact.getName());
            repositoryContact.setPhoneNumber(contactPhoneNumber);

            return GeneralResponse.getSuccessResponse();
        }
    }

    @Override
    public GeneralResponse deleteContact(int id) {
        synchronized (contacts) {
            Contact currentContact = contacts.stream()
                    .filter(c -> c.getId() == id).findFirst().orElse(null);

            if (currentContact == null) {
                return GeneralResponse.getErrorResponse("Контакт с id = " + id + " не найден.");
            }

            contacts.remove(currentContact);

            return GeneralResponse.getSuccessResponse();
        }
    }
}
