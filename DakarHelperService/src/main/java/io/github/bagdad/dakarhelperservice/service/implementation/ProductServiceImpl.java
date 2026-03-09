package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.helper.ProductHelper;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ProductRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import io.github.bagdad.models.response.product.Pagination;
import io.github.bagdad.models.response.product.ProductQueryItem;
import io.github.bagdad.models.response.product.ProductQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorService vendorService;

    public ProductServiceImpl(ProductRepository productRepository, VendorService vendorService) {
        this.productRepository = productRepository;
        this.vendorService = vendorService;
    }

    @Override
    public void saveAll(Collection<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public ProductQueryResponse query(ProductQuery query) {
        ProductQueryResponse productQueryResponse = new ProductQueryResponse();

        Pageable pageable = PageRequest.of(
            query.getPageIndex() != null ? query.getPageIndex() : 0,
            query.getPageSize() != null ? query.getPageSize() : 10
        );

        Page<Product> productPage = productRepository.query(query, pageable);
        if (productPage.isEmpty()) {
            productQueryResponse.setProductData(List.of());
            productQueryResponse.setPagination(Pagination.createEmptyPagination());
            return productQueryResponse;
        }

        List<Vendor> vendors = vendorService.findAll();

        List<ProductQueryItem> productData = productPage.stream()
                .map(product -> ProductHelper.mapToProductQueryItem(product, vendors, query.getMargin()))
                .toList();
        productQueryResponse.setProductData(productData);

        Pagination pagination = Pagination.builder()
                .pageIndex(productPage.getNumber())
                .totalRecords(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
        productQueryResponse.setPagination(pagination);

        return productQueryResponse;
    }

    @Override
    public void deleteByVendorId(Long id) {
        productRepository.deleteByVendorId(id);
    }

}
