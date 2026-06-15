package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.Family;
import java.time.Clock;
import java.time.OffsetDateTime;

public final class CreateFamilyUseCase {

    private final FamilyRepository familyRepository;
    private final IdentifierGenerator identifierGenerator;
    private final Clock clock;

    public CreateFamilyUseCase(
            FamilyRepository familyRepository,
            IdentifierGenerator identifierGenerator,
            Clock clock
    ) {
        this.familyRepository = familyRepository;
        this.identifierGenerator = identifierGenerator;
        this.clock = clock;
    }

    public Family execute(CreateFamilyCommand command) {
        Family family = Family.create(
                identifierGenerator.nextIdentifier(),
                command.name(),
                command.currency(),
                OffsetDateTime.now(clock)
        );
        return familyRepository.save(family);
    }
}
