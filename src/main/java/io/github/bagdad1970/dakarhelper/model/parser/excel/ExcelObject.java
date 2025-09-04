package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;

import java.util.*;
import java.util.stream.Collectors;

public class ExcelObject {

    private Map<String, Object> props;

    public ExcelObject() {
        this.props = new HashMap<>();
    }

    public ExcelObject(Map<String, Object> props) {
        this.props = props;
    }

    public void addValue(String key, Object value) {
        props.put(key, value);
    }

    public void removeValue(String key) {
        props.remove(key);
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public Object getProp(String key) {
        return props.get(key);
    }

    public Map<String, Integer> getCounts() {
        return props.keySet().stream()
                .filter(key -> key.startsWith("count"))
                .collect(Collectors.toMap(
                        key -> key,
                        key -> (Integer) props.get(key)
                ));
    }

    public void replacePriceProps(Set<String> newPriceKeys) {
        if (props.containsKey("price")) {
            double priceValue = (double) props.get("price");
            for (String newPriceKey : newPriceKeys) {
                props.put(newPriceKey, priceValue);
            }
            props.remove("price");
        }
        else {
            for (String newPriceKey : newPriceKeys) {
                if ( !props.containsKey(newPriceKey) )
                    props.put(newPriceKey, null);
            }
        }
    }

    public void replaceQuantityProps(Map<String, Integer> newQuantityProps) {
        for (String newProp : newQuantityProps.keySet()) {
            if ( !props.containsKey(newProp) ) {
                if ( newProp.startsWith("count") )
                    props.put(newProp, 0);
            }
        }
    }

    public Map<String, Double> getPrices() {
        Map<String, Double> prices = new HashMap<>();

        for (String key : props.keySet()) {
            if (key.equals("price") || key.equals("retail") || key.equals("wholesale") || key.equals("internet")) {
                prices.put(key, (double) props.get(key));
            }
        }
        return prices;
    }

    private boolean validateCount(SearchConditions conditions) {
        int countCondition = conditions.getCount();
        if (countCondition == 0) {
            return true;
        }

        List<Integer> countValues = (List<Integer>) getCounts().values();
        for (int countValue : countValues) {
            if (countValue >= countCondition)
                return true;
        }
        return false;
    }

    private boolean validateName(SearchConditions conditions) {
        String nameCondition = conditions.getName();
        if (nameCondition == null || nameCondition.isEmpty()) {
            return true;
        }

        String name = props.get("name").toString();
        return name.contains(nameCondition);
    }

    public boolean validateConditions(SearchConditions conditions) {
        return validateName(conditions) && validateCount(conditions);
    }

    @Override
    public String toString() {
        return props.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExcelObject that = (ExcelObject) o;
        return Objects.equals(props, that.props);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(props);
    }
}
