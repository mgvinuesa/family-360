package dev.mgvinuesa.family360.family.api;

import dev.mgvinuesa.family360.family.api.v1.FamilyMembersApi;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMember;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberInput;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberPage;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberPatch;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.in.FamilyMemberOperations;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamilyMembersApiAdapter implements FamilyMembersApi {

    private final FamilyMemberOperations familyMemberOperations;

    public FamilyMembersApiAdapter(FamilyMemberOperations familyMemberOperations) {
        this.familyMemberOperations = familyMemberOperations;
    }

    @Override
    public ResponseEntity<FamilyMember> createFamilyMember(UUID familyId, FamilyMemberInput input) {
        FamilyMember member = FamilyApiMapper.toApi(familyMemberOperations.createFamilyMember(
                new CreateFamilyMemberCommand(
                        familyId,
                        input.getName(),
                        FamilyMemberType.valueOf(input.getMemberType().name()),
                        input.getBirthDate()
                )
        ));
        URI location = URI.create("/api/v1/families/" + familyId + "/members/" + member.getId());
        return ResponseEntity.created(location).body(member);
    }

    @Override
    public ResponseEntity<Void> disableFamilyMember(UUID familyId, UUID memberId) {
        familyMemberOperations.disableFamilyMember(familyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<FamilyMember> getFamilyMember(UUID familyId, UUID memberId) {
        return ResponseEntity.ok(FamilyApiMapper.toApi(
                familyMemberOperations.getFamilyMember(familyId, memberId)
        ));
    }

    @Override
    public ResponseEntity<FamilyMemberPage> listFamilyMembers(
            UUID familyId,
            Integer page,
            Integer limit,
            String sort
    ) {
        return ResponseEntity.ok(FamilyApiMapper.toMemberPage(
                familyMemberOperations.listFamilyMembers(familyId, new PageQuery(page, limit, sort))
        ));
    }

    @Override
    public ResponseEntity<FamilyMember> patchFamilyMember(
            UUID familyId,
            UUID memberId,
            FamilyMemberPatch patch
    ) {
        FamilyMemberType memberType = patch.getMemberType() == null
                ? null
                : FamilyMemberType.valueOf(patch.getMemberType().name());
        return ResponseEntity.ok(FamilyApiMapper.toApi(familyMemberOperations.updateFamilyMember(
                new UpdateFamilyMemberCommand(
                        familyId,
                        memberId,
                        patch.getName(),
                        memberType,
                        patch.getBirthDate()
                )
        )));
    }
}
