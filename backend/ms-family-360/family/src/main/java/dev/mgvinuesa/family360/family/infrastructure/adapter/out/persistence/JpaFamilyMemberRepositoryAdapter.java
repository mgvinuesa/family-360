package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.out.FamilyMemberRepository;
import dev.mgvinuesa.family360.family.domain.FamilyMember;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
class JpaFamilyMemberRepositoryAdapter implements FamilyMemberRepository {

    private final FamilyMemberJpaRepository familyMemberJpaRepository;
    private final PageableFactory pageableFactory = new PageableFactory(
            Map.of(
                    "name", "name",
                    "memberType", "memberType",
                    "birthDate", "birthDate",
                    "createdAt", "createdAt"
            ),
            Sort.by(Sort.Direction.ASC, "name")
    );

    JpaFamilyMemberRepositoryAdapter(FamilyMemberJpaRepository familyMemberJpaRepository) {
        this.familyMemberJpaRepository = familyMemberJpaRepository;
    }

    @Override
    @Transactional
    public FamilyMember save(FamilyMember member) {
        FamilyMemberJpaEntity saved = familyMemberJpaRepository.save(FamilyPersistenceMapper.toEntity(member));
        return FamilyPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FamilyMember> findByIdAndFamilyId(UUID memberId, UUID familyId) {
        return familyMemberJpaRepository.findByIdAndFamilyId(memberId, familyId)
                .map(FamilyPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<FamilyMember> findActiveByFamilyId(UUID familyId, PageQuery pageQuery) {
        Page<FamilyMemberJpaEntity> page = familyMemberJpaRepository.findByFamilyIdAndDisabledAtIsNull(
                familyId,
                pageableFactory.create(pageQuery)
        );
        List<FamilyMember> data = page.getContent().stream().map(FamilyPersistenceMapper::toDomain).toList();
        return new PageResult<>(
                pageQuery.page(),
                pageQuery.limit(),
                page.getTotalElements(),
                page.getTotalPages(),
                data
        );
    }
}
