package io.github.bagdad1970.dakarhelper.datasource;

public class SearchConditions {

    private final String name;
    private final int count;

    public SearchConditions(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

}
