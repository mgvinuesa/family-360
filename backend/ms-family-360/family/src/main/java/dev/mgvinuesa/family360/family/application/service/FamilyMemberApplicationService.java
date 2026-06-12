package dev.mgvinuesa.family360.family.application.service;

import dev.mgvinuesa.family360.family.application.exception.FamilyMemberNotFoundException;
import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.in.FamilyMemberOperations;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.out.AuthenticatedUserProvider;
import dev.mgvinuesa.family360.family.application.port.out.FamilyAccessPort;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class FamilyMemberApplicationService implements FamilyMemberOperations {

    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final IdentifierGenerator identifierGenerator;
    private final Clock clock;
    private final FamilyAuthorizer authorizer;

    public FamilyMemberApplicationService(
            FamilyRepository familyRepository,
            FamilyMemberRepository familyMemberRepository,
            FamilyAccessPort familyAccessPort,
            AuthenticatedUserProvider authenticatedUserProvider,
            IdentifierGenerator identifierGenerator,
            Clock clock
    ) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.identifierGenerator = identifierGenerator;
        this.clock = clock;
        this.authorizer = new FamilyAuthorizer(familyAccessPort);
    }

    @Override
    public FamilyMember createFamilyMember(CreateFamilyMemberCommand command) {
        requireManage(command.familyId());
        ensureFamilyExists(command.familyId());
        FamilyMember member = FamilyMember.create(
                identifierGenerator.nextIdentifier(),
                command.familyId(),
                command.name(),
                command.memberType(),
                command.birthDate(),
                OffsetDateTime.now(clock)
        );
        return familyMemberRepository.save(member);
    }

    @Override
    public FamilyMember getFamilyMember(UUID familyId, UUID memberId) {
        requireRead(familyId);
        return findMember(familyId, memberId);
    }

    @Override
    public PageResult<FamilyMember> listFamilyMembers(UUID familyId, PageQuery pageQuery) {
        requireRead(familyId);
        ensureFamilyExists(familyId);
        return familyMemberRepository.findActiveByFamilyId(familyId, pageQuery);
    }

    @Override
    public FamilyMember updateFamilyMember(UpdateFamilyMemberCommand command) {
        requireManage(command.familyId());
        FamilyMember member = findMember(command.familyId(), command.memberId())
                .update(command.name(), command.memberType(), command.birthDate(), OffsetDateTime.now(clock).toLocalDate());
        return familyMemberRepository.save(member);
    }

    @Override
    public void disableFamilyMember(UUID familyId, UUID memberId) {
        requireManage(familyId);
        FamilyMember member = findMember(familyId, memberId).disable(OffsetDateTime.now(clock));
        familyMemberRepository.save(member);
    }

    private void requireRead(UUID familyId) {
        authorizer.requireRead(authenticatedUserProvider.currentUserId(), familyId);
    }

    private void requireManage(UUID familyId) {
        authorizer.requireManage(authenticatedUserProvider.currentUserId(), familyId);
    }

    private void ensureFamilyExists(UUID familyId) {
        familyRepository.findById(familyId).orElseThrow(() -> new FamilyNotFoundException(familyId));
    }

    private FamilyMember findMember(UUID familyId, UUID memberId) {
        return familyMemberRepository.findByIdAndFamilyId(memberId, familyId)
                .orElseThrow(() -> new FamilyMemberNotFoundException(memberId));
    }
}
