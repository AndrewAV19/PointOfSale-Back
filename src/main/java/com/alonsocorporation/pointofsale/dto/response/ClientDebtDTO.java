package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ClientDebtDTO {
    private ClientDTO client;
    private List<ProductsDTO> products;
    private Double totalAmount;
    private Double paidAmount;
    private Double remainingBalance;
    private String state;

    public ClientDebtDTO(ClientDTO client, List<ProductsDTO> products, Double totalAmount, Double paidAmount, Double remainingBalance, String state) {
        this.client = client;
        this.products = products;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.remainingBalance = remainingBalance;
        this.state = state;
    }
}