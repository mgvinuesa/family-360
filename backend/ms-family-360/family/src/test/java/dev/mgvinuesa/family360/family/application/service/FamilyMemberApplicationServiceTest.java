package dev.mgvinuesa.family360.family.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.out.AuthenticatedUserProvider;
import dev.mgvinuesa.family360.family.application.port.out.FamilyAccessPort;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.Family;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import dev.mgvinuesa.family360.family.domain.FamilyRole;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FamilyMemberApplicationServiceTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-06-12T10:00:00Z"), ZoneOffset.UTC);

    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private FamilyMemberRepository familyMemberRepository;
    @Mock
    private FamilyAccessPort familyAccessPort;
    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
    @Mock
    private IdentifierGenerator identifierGenerator;

    private FamilyMemberApplicationService service;

    @BeforeEach
    void setUp() {
        service = new FamilyMemberApplicationService(
                familyRepository,
                familyMemberRepository,
                familyAccessPort,
                authenticatedUserProvider,
                identifierGenerator,
                CLOCK
        );
    }

    @Test
    void adminCanCreateMemberInsideAccessibleFamily() {
        UUID userId = UUID.randomUUID();
        UUID familyId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        when(authenticatedUserProvider.currentUserId()).thenReturn(userId);
        when(familyAccessPort.findActiveRole(userId, familyId)).thenReturn(Optional.of(FamilyRole.ADMIN));
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(
                Family.create(familyId, "Family", "EUR", OffsetDateTime.now(CLOCK))
        ));
        when(identifierGenerator.nextIdentifier()).thenReturn(memberId);
        when(familyMemberRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FamilyMember result = service.createFamilyMember(
                new CreateFamilyMemberCommand(familyId, "Ana", FamilyMemberType.ADULT, null)
        );

        ArgumentCaptor<FamilyMember> memberCaptor = ArgumentCaptor.forClass(FamilyMember.class);
        verify(familyMemberRepository).save(memberCaptor.capture());
        assertEquals(familyId, result.familyId());
        assertEquals(memberId, memberCaptor.getValue().id());
    }
}
