package io.github.bagdad1970.dakarhelper.model.parser.excel;

import org.apache.poi.ss.usermodel.Row;
import java.util.*;

public class ExcelRow {

    private Map<String, Object> values;

    ExcelRow() {
        this.values = new HashMap<>();
    }

    public void addValue(String key, Object value) {
        values.put(key, value);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public Object getValue(String key) {
        return values.get(key);
    }

    @Override
    public String toString() {
        return values.toString();
    }

}
