package dev.mgvinuesa.family360.family.application.port.out;

import dev.mgvinuesa.family360.family.domain.FamilyRole;
import java.util.Optional;
import java.util.UUID;

public interface FamilyAccessPort {

    Optional<FamilyRole> findActiveRole(UUID userId, UUID familyId);
}
