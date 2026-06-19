package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import dev.mgvinuesa.family360.family.application.model.PageResult;
import dev.mgvinuesa.family360.family.application.port.out.FamilyRepository;
import dev.mgvinuesa.family360.family.domain.Family;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
class JpaFamilyRepositoryAdapter implements FamilyRepository {

    private final FamilyJpaRepository familyJpaRepository;
    private final PageableFactory pageableFactory = new PageableFactory(
            Map.of(
                    "name", "name",
                    "currency", "currency",
                    "createdAt", "createdAt"
            ),
            Sort.by(Sort.Direction.DESC, "createdAt")
    );

    JpaFamilyRepositoryAdapter(FamilyJpaRepository familyJpaRepository) {
        this.familyJpaRepository = familyJpaRepository;
    }

    @Override
    @Transactional
    public Family save(Family family) {
        FamilyJpaEntity saved = familyJpaRepository.save(FamilyPersistenceMapper.toEntity(family));
        return FamilyPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Family> findById(UUID familyId) {
        return familyJpaRepository.findById(familyId).map(FamilyPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Family> findActive(PageQuery pageQuery) {
        Page<FamilyJpaEntity> page = familyJpaRepository.findByDisabledAtIsNull(pageableFactory.create(pageQuery));
        List<Family> data = page.getContent().stream().map(FamilyPersistenceMapper::toDomain).toList();
        return new PageResult<>(
                pageQuery.page(),
                pageQuery.limit(),
                page.getTotalElements(),
                page.getTotalPages(),
                data
        );
    }
}
