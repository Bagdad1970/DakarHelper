package io.github.bagdad.excelparser.headerparser.headerparser;

import io.github.bagdad.excelparser.headerparser.headerparser.columnparsers.Parser;
import io.github.bagdad.excelparser.headerparser.headerparser.columnparsers.NameParser;
import io.github.bagdad.excelparser.headerparser.headerparser.columnparsers.PriceParser;
import io.github.bagdad.excelparser.headerparser.headerparser.columnparsers.QuantityParser;
import io.github.bagdad.excelparser.headerparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;

import java.util.HashMap;
import java.util.Map;

public class ParserFactory {

    private final Map<Category, Parser> parserMap;

    public ParserFactory(SubcategoryMapping nameMapping, SubcategoryMapping priceMapping, SubcategoryMapping quantityMapping) {
        this.parserMap = new HashMap<>();
        this.parserMap.put(Category.NAME, new NameParser(nameMapping));
        this.parserMap.put(Category.PRICE, new PriceParser(priceMapping));
        this.parserMap.put(Category.QUANTITY, new QuantityParser(quantityMapping));
    }

    public Parser getParserByCategory(Category category) {
        return parserMap.get(category);
    }

}
