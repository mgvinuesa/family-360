package dev.mgvinuesa.family360.family.infrastructure.adapter.in.http;

import dev.mgvinuesa.family360.family.api.v1.FamilyMembersApi;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMember;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberInput;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberPage;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyMemberPatch;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.usecase.CreateFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.DisableFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.GetFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.ListFamilyMembersUseCase;
import dev.mgvinuesa.family360.family.application.usecase.UpdateFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamilyMembersApiAdapter implements FamilyMembersApi {

    private final CreateFamilyMemberUseCase createFamilyMemberUseCase;
    private final GetFamilyMemberUseCase getFamilyMemberUseCase;
    private final ListFamilyMembersUseCase listFamilyMembersUseCase;
    private final UpdateFamilyMemberUseCase updateFamilyMemberUseCase;
    private final DisableFamilyMemberUseCase disableFamilyMemberUseCase;

    public FamilyMembersApiAdapter(
            CreateFamilyMemberUseCase createFamilyMemberUseCase,
            GetFamilyMemberUseCase getFamilyMemberUseCase,
            ListFamilyMembersUseCase listFamilyMembersUseCase,
            UpdateFamilyMemberUseCase updateFamilyMemberUseCase,
            DisableFamilyMemberUseCase disableFamilyMemberUseCase
    ) {
        this.createFamilyMemberUseCase = createFamilyMemberUseCase;
        this.getFamilyMemberUseCase = getFamilyMemberUseCase;
        this.listFamilyMembersUseCase = listFamilyMembersUseCase;
        this.updateFamilyMemberUseCase = updateFamilyMemberUseCase;
        this.disableFamilyMemberUseCase = disableFamilyMemberUseCase;
    }

    @Override
    public ResponseEntity<FamilyMember> createFamilyMember(UUID familyId, FamilyMemberInput input) {
        FamilyMember member = FamilyApiMapper.toApi(createFamilyMemberUseCase.execute(
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
        disableFamilyMemberUseCase.execute(familyId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<FamilyMember> getFamilyMember(UUID familyId, UUID memberId) {
        return ResponseEntity.ok(FamilyApiMapper.toApi(
                getFamilyMemberUseCase.execute(familyId, memberId)
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
                listFamilyMembersUseCase.execute(familyId, new PageQuery(page, limit, sort))
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
        return ResponseEntity.ok(FamilyApiMapper.toApi(updateFamilyMemberUseCase.execute(
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
