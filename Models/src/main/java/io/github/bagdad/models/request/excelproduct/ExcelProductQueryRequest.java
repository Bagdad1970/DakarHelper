package io.github.bagdad.models.request.excelproduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExcelProductQueryRequest {

    private List<Long> vendorIds;

    private Map<String, String> names;

    private Map<String, BigDecimal> prices;

    private Map<String, Integer> quantities;

}
