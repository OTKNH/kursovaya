package com.example.phonebook;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ContactSortTest {

    @Test
    void testSortByName() {
        List<Contact> list = new ArrayList<>();
        Contact c1 = new Contact("Петр Петров");
        Contact c2 = new Contact("Иван Иванов");
        list.add(c1);
        list.add(c2);

        list.sort((ct1, ct2) -> ct1.getFullName().compareToIgnoreCase(ct2.getFullName()));

        Assertions.assertEquals("Иван Иванов", list.get(0).getFullName());
        Assertions.assertEquals("Петр Петров", list.get(1).getFullName());
    }
}
