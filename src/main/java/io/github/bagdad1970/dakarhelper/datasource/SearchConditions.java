package io.github.bagdad1970.dakarhelper.datasource;

public record SearchConditions(String name, int count) {

    public SearchConditions {
        name = name.trim().toLowerCase();
    }

}
