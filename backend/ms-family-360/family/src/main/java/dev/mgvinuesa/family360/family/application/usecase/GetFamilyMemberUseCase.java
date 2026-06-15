package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyMemberNotFoundException;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.util.UUID;

public final class GetFamilyMemberUseCase {

    private final FamilyMemberRepository familyMemberRepository;

    public GetFamilyMemberUseCase(FamilyMemberRepository familyMemberRepository) {
        this.familyMemberRepository = familyMemberRepository;
    }

    public FamilyMember execute(UUID familyId, UUID memberId) {
        return familyMemberRepository.findByIdAndFamilyId(memberId, familyId)
                .orElseThrow(() -> new FamilyMemberNotFoundException(memberId));
    }
}
