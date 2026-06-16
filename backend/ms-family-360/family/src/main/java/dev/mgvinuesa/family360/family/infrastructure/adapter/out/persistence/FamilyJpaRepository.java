package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface FamilyJpaRepository extends JpaRepository<FamilyJpaEntity, UUID> {

    Page<FamilyJpaEntity> findByDisabledAtIsNull(Pageable pageable);
}
