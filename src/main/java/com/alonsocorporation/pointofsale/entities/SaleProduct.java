package com.alonsocorporation.pointofsale.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sale_products")
public class SaleProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sales sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    private Integer quantity;

    private Double discount;
}