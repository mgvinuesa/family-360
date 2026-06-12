package dev.mgvinuesa.family360.family.application.port.in;

import java.util.UUID;

public record UpdateFamilyCommand(UUID familyId, String name, String currency) {
}
