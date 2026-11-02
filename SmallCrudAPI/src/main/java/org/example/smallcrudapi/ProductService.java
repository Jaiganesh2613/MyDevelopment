package org.example.smallcrudapi;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {

        Optional<Category> category = categoryRepository.findById(productRequest.getCategoryId());
        if(category.isPresent()) {
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setQuantity(productRequest.getQuantity());
            product.setCategory(category.get());
            Product savedProduct = productRepository.save(product);
            return productMapper.toResponse(savedProduct);
        }

        throw new CategoryNotFoundException(
                "Category with id " + productRequest.getCategoryId() + " not found"
        );
    }

    public List<ProductResponse> getProducts() {

        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductByID(Long id) {

        Optional<Product> result = productRepository.findById(id);

        if (result.isPresent()) {
            return productMapper.toResponse(result.get());
        }

        throw new ProductNotFoundException(
                "Product with id " + id + " not found"
        );
    }

    public ProductResponse updateProduct(ProductRequest productRequest, Long id) {

        Optional<Product> result = productRepository.findById(id);

        if (result.isPresent()) {

            Optional<Category> category = categoryRepository.findById(productRequest.getCategoryId());

            if (category.isPresent()) {

                Product existingProduct = result.get();

                existingProduct.setName(productRequest.getName());
                existingProduct.setPrice(productRequest.getPrice());
                existingProduct.setQuantity(productRequest.getQuantity());
                existingProduct.setCategory(category.get());

                Product updatedProduct = productRepository.save(existingProduct);
                return productMapper.toResponse(updatedProduct);
            }

            throw new CategoryNotFoundException(
                    "Category with id " + productRequest.getCategoryId() + " not found"
            );
        }

        throw new ProductNotFoundException(
                "Product with id " + id + " not found"
        );
    }

    public void deleteProductByID(Long id) {

        Optional<Product> result = productRepository.findById(id);

        if (result.isPresent()) {
            productRepository.deleteById(id);
            return;
        }

        throw new ProductNotFoundException(
                "Product with id " + id + " not found"
        );
    }
}