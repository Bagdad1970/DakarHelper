package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.helper.ProductHelper;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.ProductQuery;
import io.github.bagdad.dakarhelperservice.model.Vendor;
import io.github.bagdad.dakarhelperservice.repository.interfaces.ProductRepository;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import io.github.bagdad.models.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
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
    public List<ProductResponse> query(ProductQuery query) {
        List<Product> products = productRepository.query(query);

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        List<Vendor> vendors = vendorService.findAll();

        return products.stream()
                .map(product -> ProductHelper.mapToProductResponse(product, vendors, query.getMargin()))
                .toList();
    }

    @Override
    public void deleteByVendorId(Long id) {
        productRepository.deleteByVendorId(id);
    }

}
