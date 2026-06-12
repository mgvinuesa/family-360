package dev.mgvinuesa.family360.family.application.service;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.in.FamilyOperations;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.out.AuthenticatedUserProvider;
import dev.mgvinuesa.family360.family.application.port.out.FamilyAccessPort;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.Family;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class FamilyApplicationService implements FamilyOperations {

    private final FamilyRepository familyRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final IdentifierGenerator identifierGenerator;
    private final Clock clock;
    private final FamilyAuthorizer authorizer;

    public FamilyApplicationService(
            FamilyRepository familyRepository,
            FamilyAccessPort familyAccessPort,
            AuthenticatedUserProvider authenticatedUserProvider,
            IdentifierGenerator identifierGenerator,
            Clock clock
    ) {
        this.familyRepository = familyRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.identifierGenerator = identifierGenerator;
        this.clock = clock;
        this.authorizer = new FamilyAuthorizer(familyAccessPort);
    }

    @Override
    public Family createFamily(CreateFamilyCommand command) {
        UUID currentUserId = authenticatedUserProvider.currentUserId();
        Family family = Family.create(
                identifierGenerator.nextIdentifier(),
                command.name(),
                command.currency(),
                OffsetDateTime.now(clock)
        );
        return familyRepository.createWithOwner(family, currentUserId);
    }

    @Override
    public Family getFamily(UUID familyId) {
        UUID currentUserId = authenticatedUserProvider.currentUserId();
        authorizer.requireRead(currentUserId, familyId);
        return findFamily(familyId);
    }

    @Override
    public PageResult<Family> listFamilies(PageQuery pageQuery) {
        return familyRepository.findActiveByUserId(authenticatedUserProvider.currentUserId(), pageQuery);
    }

    @Override
    public Family updateFamily(UpdateFamilyCommand command) {
        UUID currentUserId = authenticatedUserProvider.currentUserId();
        authorizer.requireManage(currentUserId, command.familyId());
        Family family = findFamily(command.familyId()).update(command.name(), command.currency());
        return familyRepository.save(family);
    }

    @Override
    public void disableFamily(UUID familyId) {
        UUID currentUserId = authenticatedUserProvider.currentUserId();
        authorizer.requireOwner(currentUserId, familyId);
        familyRepository.save(findFamily(familyId).disable(OffsetDateTime.now(clock)));
    }

    private Family findFamily(UUID familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new FamilyNotFoundException(familyId));
    }
}
