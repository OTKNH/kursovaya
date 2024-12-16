package com.example.phonebook;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения данных о контакте (ФИО и несколько номеров).
 */
public class Contact implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fullName;
    private final List<PhoneNumber> phoneNumbers = new ArrayList<>();

    /**
     * Создаёт контакт с заданным ФИО.
     * @param fullName Полное имя абонента
     */
    public Contact(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Возвращает ФИО.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Устанавливает ФИО.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Возвращает список номеров контакта.
     */
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Добавляет номер к контакту.
     */
    public void addPhoneNumber(PhoneNumber pn) {
        phoneNumbers.add(pn);
    }
}
