package ru.academits.orlov.phonebookspringboot.repositories;

import org.springframework.stereotype.Repository;
import ru.academits.orlov.phonebookspringboot.entities.Contact;
import ru.academits.orlov.phonebookspringboot.payload.GeneralResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Repository
public class ContactsInMemoryRepository implements ContactsRepository {
    public static final List<Contact> contacts = new ArrayList<>();
    private static final AtomicInteger newId = new AtomicInteger();

    @Override
    public List<Contact> getContacts(String term) {
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

    @Override
    public GeneralResponse saveContact(Contact contact) {
        if (contacts.stream()
                .anyMatch(c -> c.getPhoneNumber().toLowerCase().equals(contact.getPhoneNumber()))) {
            return new GeneralResponse(false, "Контакт с таким номером телефона уже существует.");
        }

        contact.setId(newId.incrementAndGet());
        contacts.add(contact);

        return new GeneralResponse(true, "");
    }

    @Override
    public GeneralResponse updateContact(Contact contact) {
        int contactId = contact.getId();

        if (IntStream.range(1, contacts.size() + 1).noneMatch(c -> contactId == c)) {
            return new GeneralResponse(false, "Контакт с таким id не существует.");
        }

        Contact existingContact = contacts.get(contactId - 1);

        if (!contact.getPhoneNumber().equalsIgnoreCase(existingContact.getPhoneNumber())
                && contacts.stream()
                .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contact.getPhoneNumber()))) {
            return new GeneralResponse(false, "Контакт с таким номером телефона уже существует.");
        }

        existingContact.setSurname(contact.getSurname());
        existingContact.setName(contact.getName());
        existingContact.setPhoneNumber(contact.getPhoneNumber());

        return new GeneralResponse(true, null);
    }

    @Override
    public GeneralResponse deleteContact(int id) {
        if (!contacts.removeIf(contact -> contact.getId() == id)) {
            return new GeneralResponse(false, "Не удалось удалить контакт.");
        }

        IntStream.range(id - 1, contacts.size())
                .forEach(i -> contacts.get(i).setId(contacts.get(i).getId() - 1));

        newId.decrementAndGet();

        return new GeneralResponse(true, null);
    }
}
