package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import com.alonsocorporation.pointofsale.entities.Products;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductsDTO {
    private Long id;
    private String barCode;
    private String name;
    private Double price;
    private Integer stock;
    private Double costPrice;
    private Integer quantity;
    private String image;

    public ProductsDTO(Products product) {
        this.id = product.getId();
        this.barCode = product.getBarCode();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.costPrice = product.getCostPrice();
        this.image = product.getImage();
    }

    public ProductsDTO(Products product, Integer quantity) {
        this.id = product.getId();
        this.barCode = product.getBarCode();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.costPrice = product.getCostPrice();
        this.quantity = quantity;
        this.image = product.getImage();
    }
}