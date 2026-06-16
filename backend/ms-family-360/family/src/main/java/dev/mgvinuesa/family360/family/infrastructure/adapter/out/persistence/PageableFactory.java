package dev.mgvinuesa.family360.family.infrastructure.adapter.out.persistence;

import dev.mgvinuesa.family360.family.application.model.PageQuery;
import java.util.Locale;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

final class PageableFactory {

    private final Map<String, String> sortableProperties;
    private final Sort defaultSort;

    PageableFactory(Map<String, String> sortableProperties, Sort defaultSort) {
        this.sortableProperties = Map.copyOf(sortableProperties);
        this.defaultSort = defaultSort;
    }

    Pageable create(PageQuery query) {
        return PageRequest.of(query.page(), query.limit(), sort(query.sort()));
    }

    private Sort sort(String expression) {
        if (expression == null || expression.isBlank()) {
            return defaultSort;
        }
        String[] parts = expression.split(",", 2);
        String property = sortableProperties.get(parts[0].trim());
        if (property == null) {
            return defaultSort;
        }
        Sort.Direction direction = parts.length == 2 && "desc".equals(parts[1].trim().toLowerCase(Locale.ROOT))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(direction, property);
    }
}
