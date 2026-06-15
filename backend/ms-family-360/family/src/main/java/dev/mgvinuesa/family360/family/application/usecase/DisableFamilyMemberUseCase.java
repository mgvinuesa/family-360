package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyMemberNotFoundException;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class DisableFamilyMemberUseCase {

    private final FamilyMemberRepository familyMemberRepository;
    private final Clock clock;

    public DisableFamilyMemberUseCase(FamilyMemberRepository familyMemberRepository, Clock clock) {
        this.familyMemberRepository = familyMemberRepository;
        this.clock = clock;
    }

    public void execute(UUID familyId, UUID memberId) {
        var member = familyMemberRepository.findByIdAndFamilyId(memberId, familyId)
                .orElseThrow(() -> new FamilyMemberNotFoundException(memberId));
        familyMemberRepository.save(member.disable(OffsetDateTime.now(clock)));
    }
}
