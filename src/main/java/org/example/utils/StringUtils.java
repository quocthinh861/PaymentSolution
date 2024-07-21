package org.example.utils;

public class StringUtils {
    public static String generatePlaceholders(int size) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < size; i++) {
            placeholders.append("?");
            if (i < size - 1) {
                placeholders.append(",");
            }
        }
        return placeholders.toString();
    }
}
