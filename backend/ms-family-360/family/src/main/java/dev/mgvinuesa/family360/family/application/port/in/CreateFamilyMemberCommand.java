package dev.mgvinuesa.family360.family.application.port.in;

import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import java.time.LocalDate;
import java.util.UUID;

public record CreateFamilyMemberCommand(
        UUID familyId,
        String name,
        FamilyMemberType memberType,
        LocalDate birthDate
) {
}
