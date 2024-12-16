package com.example.phonebook;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс, представляющий телефонный номер с типом.
 */
public class PhoneNumber implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String number;
    private ContactType type;

    /**
     * Создаёт телефонный номер с заданным номером и типом.
     * @param number строка номера
     * @param type тип номера
     */
    public PhoneNumber(String number, ContactType type) {
        this.number = number;
        this.type = type;
    }

    /**
     * Возвращает строку номера.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Возвращает тип номера.
     */
    public ContactType getType() {
        return type;
    }

    /**
     * Устанавливает строку номера.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Устанавливает тип номера.
     */
    public void setType(ContactType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.getDisplayName() + ": " + number;
    }
}
