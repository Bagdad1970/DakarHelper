package io.github.bagdad.models.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {

    private Integer currentPage;

    private Long totalRecords;

    private Integer totalPages;

    public static Pagination createEmptyPagination() {
        return Pagination.builder()
                .currentPage(0)
                .totalRecords(0L)
                .totalPages(0)
                .build();
    }

}