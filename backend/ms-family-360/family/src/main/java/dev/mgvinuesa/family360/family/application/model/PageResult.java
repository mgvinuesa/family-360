package dev.mgvinuesa.family360.family.application.model;

import java.util.List;

public record PageResult<T>(
        int page,
        int limit,
        long totalElements,
        int totalPages,
        List<T> data
) {

    public PageResult {
        data = List.copyOf(data);
    }
}
