package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.SaleProduct;

import lombok.Data;

@Data
public class SaleProductDTO {
    private Long id;
    private ProductsDTO product;
    private Integer quantity;

    public SaleProductDTO(SaleProduct saleProduct) {
        this.id = saleProduct.getId();
        this.product = new ProductsDTO(saleProduct.getProduct());
        this.quantity = saleProduct.getQuantity();
    }
}