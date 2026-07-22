package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.headerparser.columnparsers.ArticleParser;
import io.github.bagdad.excelparser.headerparser.columnparsers.Parser;
import io.github.bagdad.excelparser.headerparser.columnparsers.NameParser;
import io.github.bagdad.excelparser.headerparser.columnparsers.PriceParser;
import io.github.bagdad.excelparser.headerparser.columnparsers.QuantityParser;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;

import java.util.HashMap;
import java.util.Map;

public class ParserFactory {

    private final Map<Category, Parser> parserMap;

    public ParserFactory() {
        this.parserMap = new HashMap<>();
    }

    public void addMapping(Category category, SubcategoryMapping mapping) {
        if (mapping == null) {
            return;
        }

        switch (category) {
            case ARTICLE -> parserMap.put(category, new ArticleParser(mapping));
            case NAME -> parserMap.put(category, new NameParser(mapping));
            case PRICE -> parserMap.put(category, new PriceParser(mapping));
            case QUANTITY -> parserMap.put(category, new QuantityParser(mapping));
        }
    }

    public Parser getParserByCategory(Category category) {
        return parserMap.get(category);
    }

}
