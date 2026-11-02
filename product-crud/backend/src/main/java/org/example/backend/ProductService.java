package org.example.backend;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found: " + id));
    }

    public Product create(Product product) {
        product.setName(product.getName().trim());
        return repository.save(product);
    }

    public Product update(Long id, Product input) {
        Product product = getById(id);
        product.setName(input.getName().trim());
        product.setPrice(input.getPrice());
        product.setQuantity(input.getQuantity());
        return repository.save(product);
    }

    public void delete(Long id) {
        Product product = getById(id);
        repository.delete(product);
    }
}