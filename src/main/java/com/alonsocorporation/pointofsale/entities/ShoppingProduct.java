package com.alonsocorporation.pointofsale.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shopping_products")
public class ShoppingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shopping_id", nullable = false)
    private Shopping shopping;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    private Integer quantity;
}