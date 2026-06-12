package dev.mgvinuesa.family360.family.domain;

import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public record Family(
        UUID id,
        String name,
        String currency,
        OffsetDateTime createdAt,
        OffsetDateTime disabledAt
) {

    public Family {
        Objects.requireNonNull(id, "id is required");
        name = validateName(name);
        currency = validateCurrency(currency);
        Objects.requireNonNull(createdAt, "createdAt is required");
    }

    public static Family create(UUID id, String name, String currency, OffsetDateTime createdAt) {
        return new Family(id, name, currency, createdAt, null);
    }

    public Family update(String newName, String newCurrency) {
        ensureActive();
        return new Family(
                id,
                newName == null ? name : newName,
                newCurrency == null ? currency : newCurrency,
                createdAt,
                disabledAt
        );
    }

    public Family disable(OffsetDateTime disabledAt) {
        ensureActive();
        return new Family(id, name, currency, createdAt, Objects.requireNonNull(disabledAt));
    }

    private void ensureActive() {
        if (disabledAt != null) {
            throw new DomainValidationException("Family is already disabled");
        }
    }

    private static String validateName(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Family name is required");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new DomainValidationException("Family name must not exceed 120 characters");
        }
        return normalized;
    }

    private static String validateCurrency(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Family currency is required");
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        try {
            return Currency.getInstance(normalized).getCurrencyCode();
        } catch (IllegalArgumentException exception) {
            throw new DomainValidationException("Family currency must be a valid ISO 4217 code");
        }
    }
}
