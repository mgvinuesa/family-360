package dev.mgvinuesa.family360.family.configuration;

import dev.mgvinuesa.family360.family.application.port.in.FamilyMemberOperations;
import dev.mgvinuesa.family360.family.application.port.in.FamilyOperations;
import dev.mgvinuesa.family360.family.application.port.out.AuthenticatedUserProvider;
import dev.mgvinuesa.family360.family.application.port.out.FamilyAccessPort;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.application.port.out.IdentifierGenerator;
import dev.mgvinuesa.family360.family.application.service.FamilyApplicationService;
import dev.mgvinuesa.family360.family.application.service.FamilyMemberApplicationService;
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
    FamilyOperations familyOperations(
            FamilyRepository familyRepository,
            FamilyAccessPort familyAccessPort,
            AuthenticatedUserProvider authenticatedUserProvider,
            IdentifierGenerator identifierGenerator,
            Clock familyClock
    ) {
        return new FamilyApplicationService(
                familyRepository,
                familyAccessPort,
                authenticatedUserProvider,
                identifierGenerator,
                familyClock
        );
    }

    @Bean
    FamilyMemberOperations familyMemberOperations(
            FamilyRepository familyRepository,
            FamilyMemberRepository familyMemberRepository,
            FamilyAccessPort familyAccessPort,
            AuthenticatedUserProvider authenticatedUserProvider,
            IdentifierGenerator identifierGenerator,
            Clock familyClock
    ) {
        return new FamilyMemberApplicationService(
                familyRepository,
                familyMemberRepository,
                familyAccessPort,
                authenticatedUserProvider,
                identifierGenerator,
                familyClock
        );
    }
}
