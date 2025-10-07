package io.github.bagdad1970.dakarhelper.datasource;

import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ExcelObjectTest {

    @Test
    public void replacePricePropsWhenHasOnlyOnePriceProp() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addProp("price", 100.0);

        Set<String> newPriceProps = new HashSet<>() {{
            add("internet");
            add("wholesale");
            add("retail");
        }};
        excelObject.replacePriceProps(newPriceProps);

        ExcelObject expectedExcelObject = new ExcelObject();
        expectedExcelObject.addProp("wholesale", 100.0);
        expectedExcelObject.addProp("internet", 100.0);
        expectedExcelObject.addProp("retail", 100.0);

        assertEquals(expectedExcelObject, excelObject);
    }

    @Test
    public void replacePricePropsWhenHaveSeveralPriceProps() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addProp("wholesale", 123.0);
        excelObject.addProp("retail", 90.0);

        Set<String> newPriceProps = new HashSet<>() {{
            add("internet");
            add("wholesale");
            add("retail");
        }};
        excelObject.replacePriceProps(newPriceProps);

        ExcelObject expectedExcelObject = new ExcelObject();
        expectedExcelObject.addProp("wholesale", 123.0);
        expectedExcelObject.addProp("internet", null);
        expectedExcelObject.addProp("retail", 90.0);

        assertEquals(expectedExcelObject, excelObject);
    }

    @Test
    public void replaceQuantityProps() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addProp("count1", 10);

        Set<String> newQuantityProps = new HashSet<>() {{
            add("count1");
            add("count2");
        }};
        excelObject.replaceQuantityProps(newQuantityProps);

        ExcelObject expectedExcelObject = new ExcelObject();
        expectedExcelObject.addProp("count1",  10);
        expectedExcelObject.addProp("count2", 0);

        assertEquals(expectedExcelObject, excelObject);
    }

    @Test
    public void getCountsWithNullValue() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addProp("count1", 10);
        excelObject.addProp("count2", null);

        Map<String, Integer> counts = excelObject.getCounts();

        Map<String, Integer> expectedCounts = new HashMap<>() {{
            put("count1", 10);
            put("count2", 0);
        }};

        assertEquals(expectedCounts, counts);
    }

}
