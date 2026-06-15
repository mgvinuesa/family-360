package dev.mgvinuesa.family360.family.application.exception;

import java.util.UUID;

public class FamilyNotFoundException extends RuntimeException {

    public FamilyNotFoundException(UUID familyId) {
        super("Family not found: " + familyId);
    }
}
