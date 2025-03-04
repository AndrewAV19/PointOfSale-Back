package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.alonsocorporation.pointofsale.entities.Shopping;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingDTO {
    private Long id;
    private SupplierDTO supplier;
    private List<ShoppingProductDTO> shoppingProducts;
    private Double amount;
    private Double total;
    private final LocalDateTime createdAt;

    public ShoppingDTO(Shopping shopping) {
        this.id = shopping.getId();
        this.supplier = shopping.getSupplier() != null ? new SupplierDTO(shopping.getSupplier()) : null;
        this.shoppingProducts = shopping.getShoppingProducts() != null
                ? shopping.getShoppingProducts().stream().map(ShoppingProductDTO::new).toList()
                : List.of();
        this.amount = shopping.getAmount();
        this.total = shopping.getTotal();
        this.createdAt = shopping.getCreatedAt();
    }
}