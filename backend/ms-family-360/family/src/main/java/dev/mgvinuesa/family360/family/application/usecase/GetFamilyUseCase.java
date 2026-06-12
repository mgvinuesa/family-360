package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.domain.Family;
import java.util.UUID;

public final class GetFamilyUseCase {

    private final FamilyRepository familyRepository;

    public GetFamilyUseCase(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public Family execute(UUID familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new FamilyNotFoundException(familyId));
    }
}
