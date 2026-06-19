package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface FamilyMemberJpaRepository extends JpaRepository<FamilyMemberJpaEntity, UUID> {

    Optional<FamilyMemberJpaEntity> findByIdAndFamilyId(UUID id, UUID familyId);

    Page<FamilyMemberJpaEntity> findByFamilyIdAndDisabledAtIsNull(UUID familyId, Pageable pageable);
}
