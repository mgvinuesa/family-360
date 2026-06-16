package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class PageableFactoryTest {

    private final PageableFactory factory = new PageableFactory(
            Map.of("name", "name", "createdAt", "createdAt"),
            Sort.by(Sort.Direction.DESC, "createdAt")
    );

    @Test
    void createsPageableWithAllowedSort() {
        var pageable = factory.create(new PageQuery(1, 25, "name,asc"));

        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(25);
        assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void fallsBackToDefaultSortForUnknownProperty() {
        var pageable = factory.create(new PageQuery(0, 20, "unknown,desc"));

        assertThat(pageable.getSort().getOrderFor("createdAt").getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}
