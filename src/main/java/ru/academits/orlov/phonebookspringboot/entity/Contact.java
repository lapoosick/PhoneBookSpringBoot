package ru.academits.orlov.phonebookspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private int id;
    private String surname;
    private String name;
    private String phoneNumber;

    public Contact(Contact contact) {
        id = contact.getId();
        surname = contact.getSurname();
        name = contact.getName();
        phoneNumber = contact.getPhoneNumber();
    }
}
