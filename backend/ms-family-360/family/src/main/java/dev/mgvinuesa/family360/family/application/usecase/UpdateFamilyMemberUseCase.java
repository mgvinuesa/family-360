package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyMemberNotFoundException;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.time.Clock;
import java.time.OffsetDateTime;

public final class UpdateFamilyMemberUseCase {

    private final FamilyMemberRepository familyMemberRepository;
    private final Clock clock;

    public UpdateFamilyMemberUseCase(FamilyMemberRepository familyMemberRepository, Clock clock) {
        this.familyMemberRepository = familyMemberRepository;
        this.clock = clock;
    }

    public FamilyMember execute(UpdateFamilyMemberCommand command) {
        FamilyMember member = familyMemberRepository.findByIdAndFamilyId(
                        command.memberId(),
                        command.familyId()
                )
                .orElseThrow(() -> new FamilyMemberNotFoundException(command.memberId()));
        return familyMemberRepository.save(member.update(
                command.name(),
                command.memberType(),
                command.birthDate(),
                OffsetDateTime.now(clock).toLocalDate()
        ));
    }
}
