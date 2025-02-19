package com.alonsocorporation.pointofsale.entities;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clients")
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private Date createdAt;

    private Date updatedAt;

}
