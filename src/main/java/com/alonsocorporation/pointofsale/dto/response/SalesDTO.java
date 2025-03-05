package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.alonsocorporation.pointofsale.entities.Sales;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesDTO {
    private Long id;
    private ClientDTO client;
    private UserDTO user;
    private List<SaleProductDTO> saleProducts;
    private Double amount;
    private String state;
    private Double total;
    private final LocalDateTime createdAt;

    public SalesDTO(Sales sale) {
        this.id = sale.getId();
        this.client = sale.getClient() != null ? new ClientDTO(sale.getClient()) : null;
        this.user = sale.getUser() != null ? new UserDTO(sale.getUser()) : null;
        this.saleProducts = sale.getSaleProducts() != null
                ? sale.getSaleProducts().stream().map(SaleProductDTO::new).toList()
                : List.of();
        this.amount = sale.getAmount();
        this.state = sale.getState();
        this.total = sale.getTotal();
        this.createdAt = sale.getCreatedAt();
    }
}