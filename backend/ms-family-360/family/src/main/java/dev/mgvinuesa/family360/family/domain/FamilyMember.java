package dev.mgvinuesa.family360.family.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public record FamilyMember(
        UUID id,
        UUID familyId,
        String name,
        FamilyMemberType memberType,
        LocalDate birthDate,
        OffsetDateTime createdAt,
        OffsetDateTime disabledAt
) {

    public FamilyMember {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(familyId, "familyId is required");
        name = validateName(name);
        Objects.requireNonNull(memberType, "memberType is required");
        Objects.requireNonNull(createdAt, "createdAt is required");
    }

    public static FamilyMember create(
            UUID id,
            UUID familyId,
            String name,
            FamilyMemberType memberType,
            LocalDate birthDate,
            OffsetDateTime createdAt
    ) {
        validateBirthDate(birthDate, createdAt.toLocalDate());
        return new FamilyMember(id, familyId, name, memberType, birthDate, createdAt, null);
    }

    public FamilyMember update(
            String newName,
            FamilyMemberType newType,
            LocalDate newBirthDate,
            LocalDate currentDate
    ) {
        ensureActive();
        LocalDate resultingBirthDate = newBirthDate == null ? birthDate : newBirthDate;
        validateBirthDate(resultingBirthDate, currentDate);
        return new FamilyMember(
                id,
                familyId,
                newName == null ? name : newName,
                newType == null ? memberType : newType,
                resultingBirthDate,
                createdAt,
                disabledAt
        );
    }

    public FamilyMember disable(OffsetDateTime disabledAt) {
        ensureActive();
        return new FamilyMember(
                id, familyId, name, memberType, birthDate, createdAt, Objects.requireNonNull(disabledAt)
        );
    }

    private void ensureActive() {
        if (disabledAt != null) {
            throw new DomainValidationException("Family member is already disabled");
        }
    }

    private static String validateName(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Family member name is required");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new DomainValidationException("Family member name must not exceed 120 characters");
        }
        return normalized;
    }

    private static void validateBirthDate(LocalDate birthDate, LocalDate currentDate) {
        if (birthDate != null && birthDate.isAfter(currentDate)) {
            throw new DomainValidationException("Family member birth date cannot be in the future");
        }
    }
}
