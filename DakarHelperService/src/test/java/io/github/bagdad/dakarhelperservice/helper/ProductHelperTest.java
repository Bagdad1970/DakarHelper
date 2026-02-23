package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.models.response.ProductResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductHelperTest {

    @Test
    void Calculating_seller_price_with_margin_returns_seller_price() {
        BigDecimal price = BigDecimal.valueOf(100.50);
        BigDecimal margin = BigDecimal.valueOf(10);

        BigDecimal res = ProductHelper.calculateSellerPrice(price, margin);

        assertThat(res).isNotNull();
        assertThat(res).isEqualByComparingTo(BigDecimal.valueOf(110.55));
    }

    @Test
    void Calculating_price_without_margin_returns_price_itself() {
        BigDecimal price = BigDecimal.valueOf(100.50);
        BigDecimal margin = null;

        BigDecimal res = ProductHelper.calculateSellerPrice(price, margin);

        assertThat(res).isNotNull();
        assertThat(res).isEqualTo(BigDecimal.valueOf(100.50));
    }

    @Test
    void Mapping_product_to_product_response_without_vendors_throws_exception() {
        List<Vendor> vendors = List.of();
        Product product = Product.builder()
                .id("1")
                .vendorId(1L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("count", 10))
                .totalQuantity(10)
                .build();
        BigDecimal margin = BigDecimal.valueOf(10);

        assertThatThrownBy(() -> ProductHelper.mapToProductResponse(product, vendors, margin))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void Mapping_product_to_product_response_returns_product_response() {
        List<Vendor> vendors = List.of(Vendor.builder()
                .id(1L)
                .title("vendor1")
                .build()
        );
        Product product = Product.builder()
                .id("1")
                .vendorId(1L)
                .name("Hankook")
                .prices(Map.of("price", BigDecimal.valueOf(10.00)))
                .minPrice(BigDecimal.valueOf(10.00))
                .quantities(Map.of("count", 10))
                .totalQuantity(10)
                .build();
        BigDecimal margin = BigDecimal.valueOf(10);

        ProductResponse res = ProductHelper.mapToProductResponse(product, vendors, margin);

        ProductResponse expected = ProductResponse.builder()
                .name("Hankook")
                .price(BigDecimal.valueOf(10.00))
                .priceWithMargin(BigDecimal.valueOf(11.00))
                .totalQuantity(10)
                .vendorTitle("vendor1")
                .build();

        assertThat(res.getName()).isEqualTo(expected.getName());
        assertThat(res.getVendorTitle()).isEqualTo(expected.getVendorTitle());
        assertThat(res.getPrice()).isEqualByComparingTo(expected.getPrice());
        assertThat(res.getTotalQuantity()).isEqualTo(expected.getTotalQuantity());
        assertThat(res.getPriceWithMargin()).isEqualByComparingTo(expected.getPriceWithMargin());
    }

}
