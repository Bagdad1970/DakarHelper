package io.github.bagdad.models.excelparser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Category {

    ARTICLE("article"),
    NAME("name"),
    PRICE("price"),
    QUANTITY("quantity");

    @Getter
    private final String name;

}
