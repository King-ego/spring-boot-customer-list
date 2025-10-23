package com.diego.list.customers.utils;

import java.text.Normalizer;

public class SlugUtil {
    public static String toSlug(String input) {
        if (input == null) { return null; }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}