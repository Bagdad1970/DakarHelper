package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.utils.Aliases;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AliasesTest {

    private final Aliases aliases = new Aliases() {{
        addAliases("name", Arrays.asList("номенклатура", "название"));
        addAliases("price", Arrays.asList("цена", "стоимость", "опт", "оптовая", "розница", "розничная", "интернет"));
    }};

    @Test
    public void hasAliasWithPositiveResult() {
        Boolean result = aliases.hasAliasByKey("name", "название");

        assertEquals(true, result);
    }

    @Test
    public void hasAliasWithNegativeResult() {
        Boolean result = aliases.hasAliasByKey("price", "остаток");

        assertEquals(false, result);
    }

    @Test
    public void getKeyByAlias() {
        String result = aliases.getKeyByAlias("оптовая");

        assertEquals("price", result);
    }

}
