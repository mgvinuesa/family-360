package dev.mgvinuesa.family360.family.api;

import dev.mgvinuesa.family360.family.api.v1.FamiliesApi;
import dev.mgvinuesa.family360.family.api.v1.model.Family;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyInput;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyPage;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyPatch;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.in.FamilyOperations;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyCommand;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamiliesApiAdapter implements FamiliesApi {

    private final FamilyOperations familyOperations;

    public FamiliesApiAdapter(FamilyOperations familyOperations) {
        this.familyOperations = familyOperations;
    }

    @Override
    public ResponseEntity<Family> createFamily(FamilyInput input) {
        Family family = FamilyApiMapper.toApi(familyOperations.createFamily(
                new CreateFamilyCommand(input.getName(), input.getCurrency())
        ));
        return ResponseEntity.created(URI.create("/api/v1/families/" + family.getId())).body(family);
    }

    @Override
    public ResponseEntity<Void> disableFamily(UUID familyId) {
        familyOperations.disableFamily(familyId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Family> getFamily(UUID familyId) {
        return ResponseEntity.ok(FamilyApiMapper.toApi(familyOperations.getFamily(familyId)));
    }

    @Override
    public ResponseEntity<FamilyPage> listFamilies(Integer page, Integer limit, String sort) {
        return ResponseEntity.ok(FamilyApiMapper.toFamilyPage(
                familyOperations.listFamilies(new PageQuery(page, limit, sort))
        ));
    }

    @Override
    public ResponseEntity<Family> patchFamily(UUID familyId, FamilyPatch patch) {
        return ResponseEntity.ok(FamilyApiMapper.toApi(familyOperations.updateFamily(
                new UpdateFamilyCommand(familyId, patch.getName(), patch.getCurrency())
        )));
    }
}
