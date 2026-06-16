package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import dev.mgvinuesa.family360.family.domain.Family;
import dev.mgvinuesa.family360.family.domain.FamilyMember;

final class FamilyPersistenceMapper {

    private FamilyPersistenceMapper() {
    }

    static FamilyJpaEntity toEntity(Family family) {
        return new FamilyJpaEntity(
                family.id(),
                family.name(),
                family.currency(),
                family.createdAt(),
                family.disabledAt()
        );
    }

    static Family toDomain(FamilyJpaEntity entity) {
        return new Family(
                entity.id(),
                entity.name(),
                entity.currency(),
                entity.createdAt(),
                entity.disabledAt()
        );
    }

    static FamilyMemberJpaEntity toEntity(FamilyMember member) {
        return new FamilyMemberJpaEntity(
                member.id(),
                member.familyId(),
                member.name(),
                member.memberType(),
                member.birthDate(),
                member.createdAt(),
                member.disabledAt()
        );
    }

    static FamilyMember toDomain(FamilyMemberJpaEntity entity) {
        return new FamilyMember(
                entity.id(),
                entity.familyId(),
                entity.name(),
                entity.memberType(),
                entity.birthDate(),
                entity.createdAt(),
                entity.disabledAt()
        );
    }
}
