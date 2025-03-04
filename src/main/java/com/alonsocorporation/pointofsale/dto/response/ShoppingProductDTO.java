package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.ShoppingProduct;
import lombok.Data;

@Data
public class ShoppingProductDTO {
    private Long id;
    private ProductsDTO product;
    private Integer quantity;

    public ShoppingProductDTO(ShoppingProduct shoppingProduct) {
        this.id = shoppingProduct.getId();
        this.product = new ProductsDTO(shoppingProduct.getProduct());
        this.quantity = shoppingProduct.getQuantity();
    }
}