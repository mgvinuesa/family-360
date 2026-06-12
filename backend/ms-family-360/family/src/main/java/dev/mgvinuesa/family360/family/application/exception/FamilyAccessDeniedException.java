package dev.mgvinuesa.family360.family.application.exception;

import java.util.UUID;

public class FamilyAccessDeniedException extends RuntimeException {

    public FamilyAccessDeniedException(UUID familyId) {
        super("Access denied to family " + familyId);
    }
}
