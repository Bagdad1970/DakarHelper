package io.github.bagdad1970.dakarhelper.datasource;

import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ExcelObjectTest {

    @Test
    public void replacePricePropsWhenHasOnlyOnePriceProp() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addValue("price", 100.0);

        Set<String> newPriceProps = new HashSet<>() {{
            add("internet");
            add("wholesale");
            add("retail");
        }};
        excelObject.replacePriceProps(newPriceProps);

        ExcelObject expectedExcelObject = new ExcelObject();
        expectedExcelObject.addValue("wholesale", 100.0);
        expectedExcelObject.addValue("internet", 100.0);
        expectedExcelObject.addValue("retail", 100.0);

        assertEquals(expectedExcelObject, excelObject);
    }

    @Test
    public void replacePricePropsWhenHaveSeveralPriceProps() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addValue("wholesale", 123.0);
        excelObject.addValue("retail", 90.0);

        Set<String> newPriceProps = new HashSet<>() {{
            add("internet");
            add("wholesale");
            add("retail");
        }};
        excelObject.replacePriceProps(newPriceProps);

        ExcelObject expectedExcelObject = new ExcelObject();
        expectedExcelObject.addValue("wholesale", 123.0);
        expectedExcelObject.addValue("internet", null);
        expectedExcelObject.addValue("retail", 90.0);

        assertEquals(expectedExcelObject, excelObject);
    }

    @Test
    public void replaceQuantityProps() {
        ExcelObject excelObject = new ExcelObject();
        excelObject.addValue("count1", 10);

        Map<String, Integer> newQuantityProps = new HashMap<>() {{
            put("count1", 10);
            put("count2", 20);
        }};
        excelObject.replaceQuantityProps(newQuantityProps);

        ExcelObject expectedExcelObject = new ExcelObject();
        expectedExcelObject.addValue("count1",10);
        expectedExcelObject.addValue("count2", 0);

        assertEquals(expectedExcelObject, excelObject);
    }

}
