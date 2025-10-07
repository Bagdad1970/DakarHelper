package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelObject {

    private static List<String> pricesNames = Arrays.asList("price", "retail", "wholesale", "internet");
    private Map<String, Object> props;

    public ExcelObject() {
        this.props = new HashMap<>();
    }

    public void addProp(String key, Object value) {
        props.put(key, value);
    }

    public boolean containsProp(String key) {
        return props.containsKey(key);
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public Object getProp(String key) {
        return props.get(key);
    }

    public Map<String, Integer> getCounts() {
        if (props == null)
            return new HashMap<>();

        return props.keySet().stream()
                .filter(key -> key.startsWith("count"))
                .collect(Collectors.toMap(
                        key -> key,
                        key -> (Integer) (props.get(key) == null ? 0 : props.get(key))
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

    public void replaceQuantityProps(Set<String> newQuantityProps) {
        for (String newQuantityProp : newQuantityProps) {
            if ( !props.containsKey(newQuantityProp) ) {
                if ( newQuantityProp.startsWith("count") )
                    props.put(newQuantityProp, 0);
            }
        }
    }

    public Set<String> getQuantityKeys() {
        Set<String> countProps = new HashSet<>();
        for (String key : props.keySet()) {
            if (key.contains("count"))
                countProps.add(key);
        }
        return countProps;
    }

    public Set<String> getPriceKeys() {
        Set<String> priceProps = new HashSet<>();

        for (String key : props.keySet()) {
            if (pricesNames.contains(key))
                priceProps.add(key);
        }
        return priceProps;
    }

    private boolean validateCount(SearchConditions conditions) {
        int countCondition = conditions.count();
        if (countCondition == 0) {
            return true;
        }

        Collection<Integer> countValues = getCounts().values();
        for (int countValue : countValues) {
            if (countValue >= countCondition)
                return true;
        }
        return false;
    }

    private boolean validateName(SearchConditions conditions) {
        String nameCondition = conditions.name();
        if (nameCondition == null || nameCondition.isEmpty()) {
            return true;
        }

        String name = props.get("name").toString().toLowerCase();
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
