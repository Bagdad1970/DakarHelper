package io.github.bagdad.models.excelparser;

public enum Category {

    NAME("name"),
    PRICE("price"),
    QUANTITY("quantity");

    private final String name;

    Category(String title) {
        this.name = title;
    }

    public String getName() {
        return name;
    }

}
