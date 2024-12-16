package com.example.phonebook;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тесты для форматирования номера.
 */
public class PhoneUtilsTest {

    @Test
    void testFormatPhoneNumber() {
        String input = "89993129909";
        String expected = "+7(999)312-99-09";
        Assertions.assertEquals(expected, PhoneUtils.formatPhoneNumber(input));

        String input2 = "+7 999 3129909";
        Assertions.assertEquals(expected, PhoneUtils.formatPhoneNumber(input2));
    }

    @Test
    void testFormatInvalidNumber() {
        String input = "12345";
        // Не соответствует формату 11 цифр
        Assertions.assertEquals("12345", PhoneUtils.formatPhoneNumber(input));
    }
}
