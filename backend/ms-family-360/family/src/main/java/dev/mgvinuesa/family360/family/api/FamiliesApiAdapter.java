package dev.mgvinuesa.family360.family.api;

import dev.mgvinuesa.family360.family.api.v1.FamiliesApi;
import dev.mgvinuesa.family360.family.api.v1.model.Family;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyInput;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyPage;
import dev.mgvinuesa.family360.family.api.v1.model.FamilyPatch;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyCommand;
import dev.mgvinuesa.family360.family.application.usecase.CreateFamilyUseCase;
import dev.mgvinuesa.family360.family.application.usecase.DisableFamilyUseCase;
import dev.mgvinuesa.family360.family.application.usecase.GetFamilyUseCase;
import dev.mgvinuesa.family360.family.application.usecase.ListFamiliesUseCase;
import dev.mgvinuesa.family360.family.application.usecase.UpdateFamilyUseCase;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamiliesApiAdapter implements FamiliesApi {

    private final CreateFamilyUseCase createFamilyUseCase;
    private final GetFamilyUseCase getFamilyUseCase;
    private final ListFamiliesUseCase listFamiliesUseCase;
    private final UpdateFamilyUseCase updateFamilyUseCase;
    private final DisableFamilyUseCase disableFamilyUseCase;

    public FamiliesApiAdapter(
            CreateFamilyUseCase createFamilyUseCase,
            GetFamilyUseCase getFamilyUseCase,
            ListFamiliesUseCase listFamiliesUseCase,
            UpdateFamilyUseCase updateFamilyUseCase,
            DisableFamilyUseCase disableFamilyUseCase
    ) {
        this.createFamilyUseCase = createFamilyUseCase;
        this.getFamilyUseCase = getFamilyUseCase;
        this.listFamiliesUseCase = listFamiliesUseCase;
        this.updateFamilyUseCase = updateFamilyUseCase;
        this.disableFamilyUseCase = disableFamilyUseCase;
    }

    @Override
    public ResponseEntity<Family> createFamily(FamilyInput input) {
        Family family = FamilyApiMapper.toApi(createFamilyUseCase.execute(
                new CreateFamilyCommand(input.getName(), input.getCurrency())
        ));
        return ResponseEntity.created(URI.create("/api/v1/families/" + family.getId())).body(family);
    }

    @Override
    public ResponseEntity<Void> disableFamily(UUID familyId) {
        disableFamilyUseCase.execute(familyId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Family> getFamily(UUID familyId) {
        return ResponseEntity.ok(FamilyApiMapper.toApi(getFamilyUseCase.execute(familyId)));
    }

    @Override
    public ResponseEntity<FamilyPage> listFamilies(Integer page, Integer limit, String sort) {
        return ResponseEntity.ok(FamilyApiMapper.toFamilyPage(
                listFamiliesUseCase.execute(new PageQuery(page, limit, sort))
        ));
    }

    @Override
    public ResponseEntity<Family> patchFamily(UUID familyId, FamilyPatch patch) {
        return ResponseEntity.ok(FamilyApiMapper.toApi(updateFamilyUseCase.execute(
                new UpdateFamilyCommand(familyId, patch.getName(), patch.getCurrency())
        )));
    }
}
