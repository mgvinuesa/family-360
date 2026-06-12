package dev.mgvinuesa.family360.family.application.usecase;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.domain.Family;

public final class ListFamiliesUseCase {

    private final FamilyRepository familyRepository;

    public ListFamiliesUseCase(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public PageResult<Family> execute(PageQuery pageQuery) {
        return familyRepository.findActive(pageQuery);
    }
}
