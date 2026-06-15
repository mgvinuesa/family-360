package dev.mgvinuesa.family360.family.application.port.out;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.domain.Family;
import java.util.Optional;
import java.util.UUID;

public interface FamilyRepository {

    Family save(Family family);

    Optional<Family> findById(UUID familyId);

    PageResult<Family> findActive(PageQuery pageQuery);
}
