package dev.mgvinuesa.family360.family.application.port.out;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.util.Optional;
import java.util.UUID;

public interface FamilyMemberRepository {

    FamilyMember save(FamilyMember member);

    Optional<FamilyMember> findByIdAndFamilyId(UUID memberId, UUID familyId);

    PageResult<FamilyMember> findActiveByFamilyId(UUID familyId, PageQuery pageQuery);
}
