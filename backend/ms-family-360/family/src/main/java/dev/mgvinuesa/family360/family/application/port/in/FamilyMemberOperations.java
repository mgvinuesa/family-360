package dev.mgvinuesa.family360.family.application.port.in;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.util.UUID;

public interface FamilyMemberOperations {

    FamilyMember createFamilyMember(CreateFamilyMemberCommand command);

    FamilyMember getFamilyMember(UUID familyId, UUID memberId);

    PageResult<FamilyMember> listFamilyMembers(UUID familyId, PageQuery pageQuery);

    FamilyMember updateFamilyMember(UpdateFamilyMemberCommand command);

    void disableFamilyMember(UUID familyId, UUID memberId);
}
