package dev.mgvinuesa.family360.family.application.service;

import dev.mgvinuesa.family360.family.application.exception.FamilyAccessDeniedException;
import dev.mgvinuesa.family360.family.application.port.out.FamilyAccessPort;
import dev.mgvinuesa.family360.family.domain.FamilyRole;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

final class FamilyAuthorizer {

    private static final Set<FamilyRole> MANAGERS = EnumSet.of(FamilyRole.OWNER, FamilyRole.ADMIN);

    private final FamilyAccessPort familyAccessPort;

    FamilyAuthorizer(FamilyAccessPort familyAccessPort) {
        this.familyAccessPort = familyAccessPort;
    }

    void requireRead(UUID userId, UUID familyId) {
        requireRole(userId, familyId, EnumSet.allOf(FamilyRole.class));
    }

    void requireManage(UUID userId, UUID familyId) {
        requireRole(userId, familyId, MANAGERS);
    }

    void requireOwner(UUID userId, UUID familyId) {
        requireRole(userId, familyId, EnumSet.of(FamilyRole.OWNER));
    }

    private void requireRole(UUID userId, UUID familyId, Set<FamilyRole> allowedRoles) {
        FamilyRole role = familyAccessPort.findActiveRole(userId, familyId)
                .orElseThrow(() -> new FamilyAccessDeniedException(familyId));
        if (!allowedRoles.contains(role)) {
            throw new FamilyAccessDeniedException(familyId);
        }
    }
}
