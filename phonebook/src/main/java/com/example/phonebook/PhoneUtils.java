package com.example.phonebook;

/**
 * Утилитарный класс для операций с номерами телефонов.
 */
public class PhoneUtils {

    /**
     * Форматирует номер телефона в стандартный формат.
     * Например: "89993129909" -> "+7(999)312-99-09"
     * @param input исходная строка номера
     * @return отформатированный номер или исходный, если не удалось
     */
    public static String formatPhoneNumber(String input) {
        String digits = input.replaceAll("\\D+", "");
        if (digits.startsWith("8")) {
            digits = "7" + digits.substring(1);
        } else if (digits.length() == 10) {
            digits = "7" + digits;
        }
        if (digits.length() == 11) {
            return "+7(" + digits.substring(1,4) + ")" +
                    digits.substring(4,7) + "-" +
                    digits.substring(7,9) + "-" +
                    digits.substring(9);
        } else {
            return input;
        }
    }
}
