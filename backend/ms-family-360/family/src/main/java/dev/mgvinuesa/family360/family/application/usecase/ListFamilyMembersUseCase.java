package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.util.UUID;

public final class ListFamilyMembersUseCase {

    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public ListFamilyMembersUseCase(
            FamilyRepository familyRepository,
            FamilyMemberRepository familyMemberRepository
    ) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
    }

    public PageResult<FamilyMember> execute(UUID familyId, PageQuery pageQuery) {
        familyRepository.findById(familyId).orElseThrow(() -> new FamilyNotFoundException(familyId));
        return familyMemberRepository.findActiveByFamilyId(familyId, pageQuery);
    }
}
