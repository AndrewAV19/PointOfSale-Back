package com.alonsocorporation.pointofsale.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "data")
public class DataPointOfSale {

    @Id
    @Column(name = "id")
    private Long id;

    private String name;
}