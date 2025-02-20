package com.alonsocorporation.pointofsale.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    private String name;
    private String description;
    @Min(0)
    private Double price;
    @Min(0)
    private Integer stock;
    private String category;
    private Long supplier;
    @Min(0)
    private Double costPrice;
    @Min(0)
    private Double discount;
    @Min(0)
    private Double taxRate;
    @ElementCollection
    private List<String> images;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
