package ru.academits.orlov.phonebookspringboot.dao;

import org.springframework.stereotype.Repository;
import ru.academits.orlov.phonebookspringboot.dto.GeneralResponse;
import ru.academits.orlov.phonebookspringboot.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Repository
public class ContactsInMemoryRepository implements ContactsRepository {
    private static final List<Contact> contacts = new ArrayList<>();
    private static final AtomicInteger newId = new AtomicInteger();
    private static final AtomicInteger newOrdinalNumber = new AtomicInteger();

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
            String contactPhoneNumber = contact.getPhoneNumber().trim();

            if (contacts.stream()
                    .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                return GeneralResponse.getErrorResponse("Контакт с таким номером телефона уже существует.");
            }

            if (contacts.stream()
                    .anyMatch(c -> c.getOrdinalNumber() == contact.getOrdinalNumber())) {
                return GeneralResponse.getErrorResponse("Контакт с таким порядковым номером уже существует.");
            }

            contacts.add(new Contact(newId.incrementAndGet(), newOrdinalNumber.incrementAndGet(),
                    contact.getSurname().trim(), contact.getName().trim(), contactPhoneNumber));

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
                return GeneralResponse.getErrorResponse("Контакт с таким id не существует.");
            }

            String contactPhoneNumber = contact.getPhoneNumber().trim();

            if (!contactPhoneNumber.equalsIgnoreCase(repositoryContact.getPhoneNumber())
                    && contacts.stream()
                    .anyMatch(c -> c.getPhoneNumber().equalsIgnoreCase(contactPhoneNumber))) {
                return GeneralResponse.getErrorResponse("Контакт с таким номером телефона уже существует.");
            }

            repositoryContact.setSurname(contact.getSurname().trim());
            repositoryContact.setName(contact.getName().trim());
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
                return GeneralResponse.getErrorResponse("Не удалось удалить контакт.");
            }

            int currentContactOrdinalNumber = currentContact.getOrdinalNumber();

            contacts.remove(currentContact);

            IntStream.range(currentContactOrdinalNumber - 1, contacts.size())
                    .forEach(i -> contacts.get(i).setOrdinalNumber(contacts.get(i).getOrdinalNumber() - 1));

            newOrdinalNumber.decrementAndGet();

            return GeneralResponse.getSuccessResponse();
        }
    }
}
