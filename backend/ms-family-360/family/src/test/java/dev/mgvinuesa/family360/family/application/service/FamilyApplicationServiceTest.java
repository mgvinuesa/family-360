package dev.mgvinuesa.family360.family.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mgvinuesa.family360.family.application.exception.FamilyAccessDeniedException;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.out.AuthenticatedUserProvider;
import dev.mgvinuesa.family360.family.application.port.out.FamilyAccessPort;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.Family;
import dev.mgvinuesa.family360.family.domain.FamilyRole;
import java.time.Clock;
import java.time.Instant;
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
class FamilyApplicationServiceTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-06-12T10:00:00Z"), ZoneOffset.UTC);

    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private FamilyAccessPort familyAccessPort;
    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
    @Mock
    private IdentifierGenerator identifierGenerator;

    private FamilyApplicationService service;

    @BeforeEach
    void setUp() {
        service = new FamilyApplicationService(
                familyRepository,
                familyAccessPort,
                authenticatedUserProvider,
                identifierGenerator,
                CLOCK
        );
    }

    @Test
    void createsFamilyAndOwnerRelationAtomicallyThroughPort() {
        UUID userId = UUID.randomUUID();
        UUID familyId = UUID.randomUUID();
        when(authenticatedUserProvider.currentUserId()).thenReturn(userId);
        when(identifierGenerator.nextIdentifier()).thenReturn(familyId);
        when(familyRepository.createWithOwner(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(userId)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Family result = service.createFamily(new CreateFamilyCommand("Family", "EUR"));

        ArgumentCaptor<Family> familyCaptor = ArgumentCaptor.forClass(Family.class);
        verify(familyRepository).createWithOwner(familyCaptor.capture(), org.mockito.ArgumentMatchers.eq(userId));
        assertEquals(familyId, result.id());
        assertEquals("Family", familyCaptor.getValue().name());
    }

    @Test
    void onlyOwnerCanDisableFamily() {
        UUID userId = UUID.randomUUID();
        UUID familyId = UUID.randomUUID();
        when(authenticatedUserProvider.currentUserId()).thenReturn(userId);
        when(familyAccessPort.findActiveRole(userId, familyId)).thenReturn(Optional.of(FamilyRole.ADMIN));

        assertThrows(FamilyAccessDeniedException.class, () -> service.disableFamily(familyId));
    }
}
