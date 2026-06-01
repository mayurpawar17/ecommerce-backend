package dev.mayur.ecommerce_backend.core.utils.dto;

import lombok.*;

@Getter
@Setter
public class Pagination<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    // getters & setters
}
