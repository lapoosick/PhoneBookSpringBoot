package ru.academits.orlov.phonebookspringboot.dao;

import org.springframework.stereotype.Repository;
import ru.academits.orlov.phonebookspringboot.entity.Contact;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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

            return contacts.stream()
                    .filter(contact -> contact.getSurname().toLowerCase().contains(term)
                            || contact.getName().toLowerCase().contains(term)
                            || contact.getPhoneNumber().toLowerCase().contains(term))
                    .toList();
        }
    }

    @Override
    public GeneralResponse saveContact(Contact contact) {
        synchronized (contacts) {
            if (contacts.stream()
                    .anyMatch(c -> c.getPhoneNumber().toLowerCase().equals(contact.getPhoneNumber()))) {
                return GeneralResponse.getErrorResponse("Контакт с таким номером телефона уже существует.");
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

            if (IntStream.range(1, contacts.size() + 1).noneMatch(i -> i == contactId)) {
                return GeneralResponse.getErrorResponse("Контакт с таким id не существует.");
            }

            Contact existingContact = contacts.get(contactId - 1);

            if (!contact.getPhoneNumber().equalsIgnoreCase(existingContact.getPhoneNumber())
                    && contacts.stream()
                    .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contact.getPhoneNumber()))) {
                return GeneralResponse.getErrorResponse("Контакт с таким номером телефона уже существует.");
            }

            existingContact.setSurname(contact.getSurname());
            existingContact.setName(contact.getName());
            existingContact.setPhoneNumber(contact.getPhoneNumber());

            return GeneralResponse.getSuccessResponse();
        }
    }

    @Override
    public GeneralResponse deleteContact(int id) {
        synchronized (contacts) {
            if (!contacts.removeIf(contact -> contact.getId() == id)) {
                return GeneralResponse.getErrorResponse("Не удалось удалить контакт.");
            }

            IntStream.range(id - 1, contacts.size())
                    .forEach(i -> contacts.get(i).setId(contacts.get(i).getId() - 1));

            newId.decrementAndGet();

            return GeneralResponse.getSuccessResponse();
        }
    }
}
