package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.List;
import com.alonsocorporation.pointofsale.entities.Products;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long id;
    private String barCode;
    private String qrCode;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private CategoryDTO category;
    private List<SupplierDTO> suppliers;
    private Double costPrice;
    private Double discount;
    private Double taxRate;
    private String image;

    public ProductDTO(Products product) {
        this.id = product.getId();
        this.barCode = product.getBarCode();
        this.qrCode = product.getQrCode();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.category = product.getCategory() != null ? new CategoryDTO(product.getCategory()) : null;
        this.suppliers = product.getSuppliers() != null
                ? product.getSuppliers().stream().map(SupplierDTO::new).toList()
                : List.of();
        this.costPrice = product.getCostPrice();
        this.discount = product.getDiscount();
        this.taxRate = product.getTaxRate();
        this.image = product.getImage();
    }
}
