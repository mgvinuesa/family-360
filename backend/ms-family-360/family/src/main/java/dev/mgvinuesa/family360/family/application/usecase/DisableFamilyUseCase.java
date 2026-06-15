package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class DisableFamilyUseCase {

    private final FamilyRepository familyRepository;
    private final Clock clock;

    public DisableFamilyUseCase(FamilyRepository familyRepository, Clock clock) {
        this.familyRepository = familyRepository;
        this.clock = clock;
    }

    public void execute(UUID familyId) {
        var family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FamilyNotFoundException(familyId));
        familyRepository.save(family.disable(OffsetDateTime.now(clock)));
    }
}
