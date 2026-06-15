package dev.mgvinuesa.family360.family.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FamilyTest {

    private static final OffsetDateTime NOW =
            OffsetDateTime.of(2026, 6, 12, 10, 0, 0, 0, ZoneOffset.UTC);

    @Test
    void normalizesNameAndCurrency() {
        Family family = Family.create(UUID.randomUUID(), "  Garcia Family  ", "eur", NOW);

        assertEquals("Garcia Family", family.name());
        assertEquals("EUR", family.currency());
    }

    @Test
    void rejectsUnknownCurrency() {
        assertThrows(
                DomainValidationException.class,
                () -> Family.create(UUID.randomUUID(), "Family", "ZZZ", NOW)
        );
    }

    @Test
    void cannotUpdateDisabledFamily() {
        Family family = Family.create(UUID.randomUUID(), "Family", "EUR", NOW).disable(NOW);

        assertThrows(DomainValidationException.class, () -> family.update("New name", null));
    }
}
