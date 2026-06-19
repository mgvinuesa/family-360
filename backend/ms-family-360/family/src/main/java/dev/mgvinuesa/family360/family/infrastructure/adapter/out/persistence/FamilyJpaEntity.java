package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "family", schema = "family")
class FamilyJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "disabled_at")
    private OffsetDateTime disabledAt;

    protected FamilyJpaEntity() {
    }

    FamilyJpaEntity(UUID id, String name, String currency, OffsetDateTime createdAt, OffsetDateTime disabledAt) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.createdAt = createdAt;
        this.disabledAt = disabledAt;
    }

    UUID id() {
        return id;
    }

    String name() {
        return name;
    }

    String currency() {
        return currency;
    }

    OffsetDateTime createdAt() {
        return createdAt;
    }

    OffsetDateTime disabledAt() {
        return disabledAt;
    }
}
