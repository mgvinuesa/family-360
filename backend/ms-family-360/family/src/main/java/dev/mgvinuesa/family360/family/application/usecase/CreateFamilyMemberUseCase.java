package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.time.Clock;
import java.time.OffsetDateTime;

public final class CreateFamilyMemberUseCase {

    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final IdentifierGenerator identifierGenerator;
    private final Clock clock;

    public CreateFamilyMemberUseCase(
            FamilyRepository familyRepository,
            FamilyMemberRepository familyMemberRepository,
            IdentifierGenerator identifierGenerator,
            Clock clock
    ) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
        this.identifierGenerator = identifierGenerator;
        this.clock = clock;
    }

    public FamilyMember execute(CreateFamilyMemberCommand command) {
        familyRepository.findById(command.familyId())
                .orElseThrow(() -> new FamilyNotFoundException(command.familyId()));
        FamilyMember member = FamilyMember.create(
                identifierGenerator.nextIdentifier(),
                command.familyId(),
                command.name(),
                command.memberType(),
                command.birthDate(),
                OffsetDateTime.now(clock)
        );
        return familyMemberRepository.save(member);
    }
}
