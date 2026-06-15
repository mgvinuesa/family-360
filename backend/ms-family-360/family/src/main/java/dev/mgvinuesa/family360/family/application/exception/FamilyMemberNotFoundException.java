package dev.mgvinuesa.family360.family.application.exception;

import java.util.UUID;

public class FamilyMemberNotFoundException extends RuntimeException {

    public FamilyMemberNotFoundException(UUID memberId) {
        super("Family member not found: " + memberId);
    }
}
