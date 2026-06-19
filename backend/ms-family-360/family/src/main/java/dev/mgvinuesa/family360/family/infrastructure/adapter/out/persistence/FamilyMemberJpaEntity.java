package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "family_member", schema = "family")
class FamilyMemberJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false, length = 20)
    private FamilyMemberType memberType;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "disabled_at")
    private OffsetDateTime disabledAt;

    protected FamilyMemberJpaEntity() {
    }

    FamilyMemberJpaEntity(
            UUID id,
            UUID familyId,
            String name,
            FamilyMemberType memberType,
            LocalDate birthDate,
            OffsetDateTime createdAt,
            OffsetDateTime disabledAt
    ) {
        this.id = id;
        this.familyId = familyId;
        this.name = name;
        this.memberType = memberType;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.disabledAt = disabledAt;
    }

    UUID id() {
        return id;
    }

    UUID familyId() {
        return familyId;
    }

    String name() {
        return name;
    }

    FamilyMemberType memberType() {
        return memberType;
    }

    LocalDate birthDate() {
        return birthDate;
    }

    OffsetDateTime createdAt() {
        return createdAt;
    }

    OffsetDateTime disabledAt() {
        return disabledAt;
    }
}
