package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import dev.mgvinuesa.family360.family.domain.Family;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FamilyPersistenceMapperTest {

    @Test
    void mapsFamilyBothWays() {
        Family family = new Family(
                UUID.fromString("2e4613df-22c0-49e3-b8fd-c7f6b500159c"),
                "Garcia",
                "EUR",
                OffsetDateTime.parse("2026-06-16T08:00:00Z"),
                OffsetDateTime.parse("2026-06-16T09:00:00Z")
        );

        FamilyJpaEntity entity = FamilyPersistenceMapper.toEntity(family);
        Family result = FamilyPersistenceMapper.toDomain(entity);

        assertThat(result).isEqualTo(family);
    }

    @Test
    void mapsFamilyMemberBothWays() {
        FamilyMember member = new FamilyMember(
                UUID.fromString("d9a39d24-43a3-47df-9e70-2586260fac53"),
                UUID.fromString("2e4613df-22c0-49e3-b8fd-c7f6b500159c"),
                "Lucia",
                FamilyMemberType.CHILD,
                LocalDate.parse("2019-02-17"),
                OffsetDateTime.parse("2026-06-16T08:00:00Z"),
                null
        );

        FamilyMemberJpaEntity entity = FamilyPersistenceMapper.toEntity(member);
        FamilyMember result = FamilyPersistenceMapper.toDomain(entity);

        assertThat(result).isEqualTo(member);
    }
}
