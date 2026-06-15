package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.domain.Family;

public final class UpdateFamilyUseCase {

    private final FamilyRepository familyRepository;

    public UpdateFamilyUseCase(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public Family execute(UpdateFamilyCommand command) {
        Family family = familyRepository.findById(command.familyId())
                .orElseThrow(() -> new FamilyNotFoundException(command.familyId()));
        return familyRepository.save(family.update(command.name(), command.currency()));
    }
}
