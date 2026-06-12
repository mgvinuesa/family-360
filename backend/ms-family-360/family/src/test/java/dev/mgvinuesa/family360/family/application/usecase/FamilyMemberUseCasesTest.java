package dev.mgvinuesa.family360.family.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.mgvinuesa.family360.family.application.exception.FamilyMemberNotFoundException;
import dev.mgvinuesa.family360.family.application.exception.FamilyNotFoundException;
import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.in.CreateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.in.UpdateFamilyMemberCommand;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.domain.Family;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import dev.mgvinuesa.family360.family.domain.FamilyMemberType;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
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
class FamilyMemberUseCasesTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-06-12T10:00:00Z"), ZoneOffset.UTC);

    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private FamilyMemberRepository familyMemberRepository;
    @Mock
    private IdentifierGenerator identifierGenerator;

    @Test
    void createFamilyMemberChecksFamilyAndSavesMember() {
        Family family = family();
        UUID memberId = UUID.randomUUID();
        when(familyRepository.findById(family.id())).thenReturn(Optional.of(family));
        when(identifierGenerator.nextIdentifier()).thenReturn(memberId);
        when(familyMemberRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FamilyMember result = new CreateFamilyMemberUseCase(
                familyRepository,
                familyMemberRepository,
                identifierGenerator,
                CLOCK
        ).execute(new CreateFamilyMemberCommand(
                family.id(),
                "Ana",
                FamilyMemberType.ADULT,
                LocalDate.of(1990, 1, 1)
        ));

        assertEquals(memberId, result.id());
        assertEquals(family.id(), result.familyId());
    }

    @Test
    void createFamilyMemberFailsWhenFamilyDoesNotExist() {
        UUID familyId = UUID.randomUUID();
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(
                FamilyNotFoundException.class,
                () -> new CreateFamilyMemberUseCase(
                        familyRepository,
                        familyMemberRepository,
                        identifierGenerator,
                        CLOCK
                ).execute(new CreateFamilyMemberCommand(
                        familyId,
                        "Ana",
                        FamilyMemberType.ADULT,
                        null
                ))
        );
    }

    @Test
    void getFamilyMemberUsesFamilyAndMemberIdentifiers() {
        FamilyMember member = member();
        when(familyMemberRepository.findByIdAndFamilyId(member.id(), member.familyId()))
                .thenReturn(Optional.of(member));

        FamilyMember result = new GetFamilyMemberUseCase(familyMemberRepository)
                .execute(member.familyId(), member.id());

        assertEquals(member, result);
    }

    @Test
    void getFamilyMemberFailsWhenScopedMemberDoesNotExist() {
        UUID familyId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        when(familyMemberRepository.findByIdAndFamilyId(memberId, familyId))
                .thenReturn(Optional.empty());

        assertThrows(
                FamilyMemberNotFoundException.class,
                () -> new GetFamilyMemberUseCase(familyMemberRepository).execute(familyId, memberId)
        );
    }

    @Test
    void listFamilyMembersChecksFamilyAndDelegatesPagination() {
        Family family = family();
        PageQuery query = new PageQuery(0, 20, null);
        PageResult<FamilyMember> page = new PageResult<>(0, 20, 1, 1, List.of(member(family.id())));
        when(familyRepository.findById(family.id())).thenReturn(Optional.of(family));
        when(familyMemberRepository.findActiveByFamilyId(family.id(), query)).thenReturn(page);

        PageResult<FamilyMember> result = new ListFamilyMembersUseCase(
                familyRepository,
                familyMemberRepository
        ).execute(family.id(), query);

        assertEquals(page, result);
    }

    @Test
    void updateFamilyMemberPersistsDomainUpdate() {
        FamilyMember member = member();
        when(familyMemberRepository.findByIdAndFamilyId(member.id(), member.familyId()))
                .thenReturn(Optional.of(member));
        when(familyMemberRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FamilyMember result = new UpdateFamilyMemberUseCase(familyMemberRepository, CLOCK)
                .execute(new UpdateFamilyMemberCommand(
                        member.familyId(),
                        member.id(),
                        "New name",
                        FamilyMemberType.DEPENDENT,
                        null
                ));

        assertEquals("New name", result.name());
        assertEquals(FamilyMemberType.DEPENDENT, result.memberType());
    }

    @Test
    void disableFamilyMemberPersistsDisabledTimestamp() {
        FamilyMember member = member();
        when(familyMemberRepository.findByIdAndFamilyId(member.id(), member.familyId()))
                .thenReturn(Optional.of(member));

        new DisableFamilyMemberUseCase(familyMemberRepository, CLOCK)
                .execute(member.familyId(), member.id());

        ArgumentCaptor<FamilyMember> captor = ArgumentCaptor.forClass(FamilyMember.class);
        verify(familyMemberRepository).save(captor.capture());
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

    private FamilyMember member() {
        return member(UUID.randomUUID());
    }

    private FamilyMember member(UUID familyId) {
        return FamilyMember.create(
                UUID.randomUUID(),
                familyId,
                "Ana",
                FamilyMemberType.ADULT,
                LocalDate.of(1990, 1, 1),
                OffsetDateTime.now(CLOCK)
        );
    }
}
