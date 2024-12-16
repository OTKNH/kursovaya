package com.example.phonebook;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class FilterTest {

    @Test
    void testFilterByPhone() {
        Contact c = new Contact("Иван Иванов");
        c.addPhoneNumber(new PhoneNumber("+7(111)222-33-44", ContactType.HOME));

        Contact c2 = new Contact("Петр Петров");
        c2.addPhoneNumber(new PhoneNumber("+7(222)333-44-55", ContactType.MOBILE));

        List<Contact> contacts = List.of(c, c2);
        String phoneQuery = "222-33-44";

        List<Contact> filtered = contacts.stream()
                .filter(ct -> ct.getPhoneNumbers().stream()
                        .anyMatch(pn -> pn.getNumber().contains(phoneQuery)))
                .collect(Collectors.toList());

        Assertions.assertEquals(1, filtered.size());
        Assertions.assertEquals("Иван Иванов", filtered.get(0).getFullName());
    }
}
