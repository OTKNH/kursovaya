package com.example.phonebook;

import java.io.Serializable;

/**
 * Тип телефонного номера (мобильный, домашний, факс, рабочий).
 */
public enum ContactType implements Serializable {
    MOBILE("Мобильный"),
    HOME("Домашний"),
    FAX("Факс"),
    WORK("Рабочий");

    private final String displayName;

    ContactType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает отображаемое имя типа.
     * @return строковое название типа номера
     */
    public String getDisplayName() {
        return displayName;
    }
}
