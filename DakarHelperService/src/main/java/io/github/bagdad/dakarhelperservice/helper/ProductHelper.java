package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.models.response.product.ProductQueryItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
                .price(product.getMinPrice())
                .totalQuantity(product.getTotalQuantity())
                .priceWithMargin(calculateSellerPrice(product.getMinPrice(), margin))
                .vendorTitle(foundVendorTitle)
                .build();
    }

    static BigDecimal calculateSellerPrice(BigDecimal price, BigDecimal margin) {
        if (margin == null) {
            return price;
        }

        BigDecimal marginPercent = margin.divide(BigDecimal.valueOf(100),
                2,
                RoundingMode.HALF_UP
        );

        return price.multiply(BigDecimal.valueOf(1).add(marginPercent));
    }

}
