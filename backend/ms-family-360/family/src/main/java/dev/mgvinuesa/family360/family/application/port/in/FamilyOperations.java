package dev.mgvinuesa.family360.family.application.port.in;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.domain.Family;
import java.util.UUID;

public interface FamilyOperations {

    Family createFamily(CreateFamilyCommand command);

    Family getFamily(UUID familyId);

    PageResult<Family> listFamilies(PageQuery pageQuery);

    Family updateFamily(UpdateFamilyCommand command);

    void disableFamily(UUID familyId);
}
