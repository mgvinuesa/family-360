package dev.mgvinuesa.family360.family.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyCommand;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.Family;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FamilyUseCasesTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-06-12T10:00:00Z"), ZoneOffset.UTC);

    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private IdentifierGenerator identifierGenerator;

    @Test
    void createFamilySavesNewFamily() {
        UUID familyId = UUID.randomUUID();
        when(identifierGenerator.nextIdentifier()).thenReturn(familyId);
        when(familyRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Family family = new CreateFamilyUseCase(familyRepository, identifierGenerator, CLOCK)
                .execute(new CreateFamilyCommand("Family", "EUR"));

        assertEquals(familyId, family.id());
        assertEquals("Family", family.name());
        verify(familyRepository).save(family);
    }

    @Test
    void getFamilyReturnsRepositoryResult() {
        Family family = family();
        when(familyRepository.findById(family.id())).thenReturn(Optional.of(family));

        Family result = new GetFamilyUseCase(familyRepository).execute(family.id());

        assertEquals(family, result);
    }

    @Test
    void getFamilyFailsWhenFamilyDoesNotExist() {
        UUID familyId = UUID.randomUUID();
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(
                FamilyNotFoundException.class,
                () -> new GetFamilyUseCase(familyRepository).execute(familyId)
        );
    }

    @Test
    void listFamiliesDelegatesPagination() {
        PageQuery query = new PageQuery(0, 20, "name,asc");
        PageResult<Family> page = new PageResult<>(0, 20, 1, 1, List.of(family()));
        when(familyRepository.findActive(query)).thenReturn(page);

        PageResult<Family> result = new ListFamiliesUseCase(familyRepository).execute(query);

        assertEquals(page, result);
    }

    @Test
    void updateFamilyPersistsDomainUpdate() {
        Family family = family();
        when(familyRepository.findById(family.id())).thenReturn(Optional.of(family));
        when(familyRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Family result = new UpdateFamilyUseCase(familyRepository)
                .execute(new UpdateFamilyCommand(family.id(), "New name", null));

        assertEquals("New name", result.name());
        assertEquals("EUR", result.currency());
    }

    @Test
    void disableFamilyPersistsDisabledTimestamp() {
        Family family = family();
        when(familyRepository.findById(family.id())).thenReturn(Optional.of(family));

        new DisableFamilyUseCase(familyRepository, CLOCK).execute(family.id());

        ArgumentCaptor<Family> captor = ArgumentCaptor.forClass(Family.class);
        verify(familyRepository).save(captor.capture());
        assertNotNull(captor.getValue().disabledAt());
    }

    private Family family() {
        return Family.create(
                UUID.randomUUID(),
                "Family",
                "EUR",
                OffsetDateTime.now(CLOCK)
        );
    }
}
