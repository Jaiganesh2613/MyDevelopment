package org.example.smallcrudapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {

        ProductResponse savedProduct = productService.createProduct(productRequest);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProducts() {

        return new ResponseEntity<>(
                productService.getProducts(),
                HttpStatus.OK
        );
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductByID(@PathVariable Long id) {

        ProductResponse product = productService.getProductByID(id);

        return new ResponseEntity<>(
                product,
                HttpStatus.OK
        );
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody @Valid ProductRequest productRequest, @PathVariable Long id) {

        ProductResponse updatedProduct = productService.updateProduct(productRequest, id);

        return new ResponseEntity<>(
                updatedProduct,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProductByID(@PathVariable Long id) {

        productService.deleteProductByID(id);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}