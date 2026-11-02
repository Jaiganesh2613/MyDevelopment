package org.example.smallcrudapi;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse (Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        return response;
    }
}
