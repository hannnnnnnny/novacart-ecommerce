package com.novacart.store.service;

import java.text.Normalizer;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

    public String createSlug(String preferredSlug, String fallbackText) {
        String source = hasText(preferredSlug) ? preferredSlug : fallbackText;
        String normalized = Normalizer.normalize(source, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        if (normalized.isBlank()) {
            return "item";
        }

        return normalized;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
