package dev.mgvinuesa.family360.family.application.model;

public record PageQuery(int page, int limit, String sort) {

    public PageQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be zero or greater");
        }
        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("limit must be between 1 and 100");
        }
    }
}
