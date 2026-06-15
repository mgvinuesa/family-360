package dev.mgvinuesa.family360.family.infrastructure.configuration;

import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.application.usecase.CreateFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.CreateFamilyUseCase;
import dev.mgvinuesa.family360.family.application.usecase.DisableFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.DisableFamilyUseCase;
import dev.mgvinuesa.family360.family.application.usecase.GetFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.GetFamilyUseCase;
import dev.mgvinuesa.family360.family.application.usecase.ListFamiliesUseCase;
import dev.mgvinuesa.family360.family.application.usecase.ListFamilyMembersUseCase;
import dev.mgvinuesa.family360.family.application.usecase.UpdateFamilyMemberUseCase;
import dev.mgvinuesa.family360.family.application.usecase.UpdateFamilyUseCase;
import java.time.Clock;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FamilyModuleConfiguration {

    @Bean
    Clock familyClock() {
        return Clock.systemUTC();
    }

    @Bean
    IdentifierGenerator familyIdentifierGenerator() {
        return UUID::randomUUID;
    }

    @Bean
    CreateFamilyUseCase createFamilyUseCase(
            FamilyRepository familyRepository,
            IdentifierGenerator identifierGenerator,
            Clock familyClock
    ) {
        return new CreateFamilyUseCase(familyRepository, identifierGenerator, familyClock);
    }

    @Bean
    GetFamilyUseCase getFamilyUseCase(FamilyRepository familyRepository) {
        return new GetFamilyUseCase(familyRepository);
    }

    @Bean
    ListFamiliesUseCase listFamiliesUseCase(FamilyRepository familyRepository) {
        return new ListFamiliesUseCase(familyRepository);
    }

    @Bean
    UpdateFamilyUseCase updateFamilyUseCase(FamilyRepository familyRepository) {
        return new UpdateFamilyUseCase(familyRepository);
    }

    @Bean
    DisableFamilyUseCase disableFamilyUseCase(FamilyRepository familyRepository, Clock familyClock) {
        return new DisableFamilyUseCase(familyRepository, familyClock);
    }

    @Bean
    CreateFamilyMemberUseCase createFamilyMemberUseCase(
            FamilyRepository familyRepository,
            FamilyMemberRepository familyMemberRepository,
            IdentifierGenerator identifierGenerator,
            Clock familyClock
    ) {
        return new CreateFamilyMemberUseCase(
                familyRepository,
                familyMemberRepository,
                identifierGenerator,
                familyClock
        );
    }

    @Bean
    GetFamilyMemberUseCase getFamilyMemberUseCase(FamilyMemberRepository familyMemberRepository) {
        return new GetFamilyMemberUseCase(familyMemberRepository);
    }

    @Bean
    ListFamilyMembersUseCase listFamilyMembersUseCase(
            FamilyRepository familyRepository,
            FamilyMemberRepository familyMemberRepository
    ) {
        return new ListFamilyMembersUseCase(familyRepository, familyMemberRepository);
    }

    @Bean
    UpdateFamilyMemberUseCase updateFamilyMemberUseCase(
            FamilyMemberRepository familyMemberRepository,
            Clock familyClock
    ) {
        return new UpdateFamilyMemberUseCase(familyMemberRepository, familyClock);
    }

    @Bean
    DisableFamilyMemberUseCase disableFamilyMemberUseCase(
            FamilyMemberRepository familyMemberRepository,
            Clock familyClock
    ) {
        return new DisableFamilyMemberUseCase(familyMemberRepository, familyClock);
    }
}
