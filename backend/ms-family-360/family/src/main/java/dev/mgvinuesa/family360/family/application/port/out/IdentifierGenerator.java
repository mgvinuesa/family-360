package dev.mgvinuesa.family360.family.application.port.out;

import java.util.UUID;

public interface IdentifierGenerator {

    UUID nextIdentifier();
}
