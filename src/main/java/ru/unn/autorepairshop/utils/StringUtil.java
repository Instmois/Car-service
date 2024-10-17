package ru.unn.autorepairshop.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

    private static final String SEMICOLON = ";";

    public static String trimToFirstDot(String input) {
        if (input == null) {
            return null;
        }

        int index = input.indexOf('.');

        if (index == -1) {
            return input;
        }

        return input.substring(index + 1, input.length() - 1);
    }

    public static String shortenStringToSemicolon(String input) {
        if (input == null || !input.contains(SEMICOLON)) {
            return input;
        }

        return input.substring(0, input.indexOf(SEMICOLON));
    }

}
