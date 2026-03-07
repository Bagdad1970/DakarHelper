package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.models.response.product.ProductQueryItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ProductHelper {

    public static ProductQueryItem mapToProductQueryItem(Product product,
                                                         List<Vendor> vendors,
                                                         BigDecimal margin) {
        if (vendors.isEmpty()) {
            throw new RuntimeException("Vendors not provided");
        }

        String foundVendorTitle = vendors.stream()
                .filter(vendor -> vendor.getId().equals(product.getVendorId()))
                .map(Vendor::getTitle)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        return ProductQueryItem.builder()
                .name(product.getName())
                .minPrice(product.getMinPrice())
                .totalQuantity(product.getTotalQuantity())
                .priceWithMargin(calculateSellerPrice(product.getMinPrice(), margin))
                .vendorTitle(foundVendorTitle)
                .build();
    }

    static BigDecimal calculateSellerPrice(BigDecimal price, BigDecimal margin) {
        if (margin == null) {
            return price;
        }

        BigDecimal multiplier = BigDecimal.valueOf(100).add(margin);

        return price.multiply(multiplier).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    public static List<Criteria> createFieldConditions(ProductQuery productQuery) {
        List<Criteria> fieldConditions = new ArrayList<>();

        fieldConditions.add(where("vendor_id").in(productQuery.getVendorIds()));

        if (productQuery.getName() != null && !productQuery.getName().isBlank()) {
            fieldConditions.add(
                    where("name")
                            .regex(Pattern.compile(productQuery.getName(), Pattern.CASE_INSENSITIVE))
            );
        }

        if (productQuery.getMinPrice() != null) {
            fieldConditions.add(
                    where("min_price")
                            .lte(productQuery.getMinPrice())
            );
        }

        if (productQuery.getQuantity() != null) {
            fieldConditions.add(
                    where("total_quantity")
                        .gte(productQuery.getQuantity())
            );
        }

        return fieldConditions;
    }

    public static List<Sort.Order> createSortingConditions(ProductQuery productQuery) {
        LinkedHashMap<String, Integer> sortMap = productQuery.getSortingConditions();

        if (sortMap == null || sortMap.isEmpty()) {
            return Collections.emptyList();
        }

        List<Sort.Order> orders = new ArrayList<>();
        sortMap.forEach((field, direction) -> {
            Sort.Order order;
            if (direction == -1) {
                order = new Sort.Order(Sort.Direction.ASC, field);
            }
            else if (direction == 1) {
                order = new Sort.Order(Sort.Direction.DESC, field);
            }
            else {
                return; // ignoring SORT_DEFAULT (0)
            }

            orders.add(order);
        });

        return orders;
    }

}
