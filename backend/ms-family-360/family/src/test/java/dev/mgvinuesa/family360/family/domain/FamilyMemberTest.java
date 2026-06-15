package dev.mgvinuesa.family360.family.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FamilyMemberTest {

    private static final OffsetDateTime NOW =
            OffsetDateTime.of(2026, 6, 12, 10, 0, 0, 0, ZoneOffset.UTC);

    @Test
    void createsMemberWithoutUserAccount() {
        FamilyMember member = FamilyMember.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Ana",
                FamilyMemberType.CHILD,
                LocalDate.of(2018, 3, 2),
                NOW
        );

        assertEquals("Ana", member.name());
        assertEquals(FamilyMemberType.CHILD, member.memberType());
    }

    @Test
    void rejectsFutureBirthDate() {
        assertThrows(
                DomainValidationException.class,
                () -> FamilyMember.create(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "Ana",
                        FamilyMemberType.CHILD,
                        LocalDate.of(2027, 1, 1),
                        NOW
                )
        );
    }
}
