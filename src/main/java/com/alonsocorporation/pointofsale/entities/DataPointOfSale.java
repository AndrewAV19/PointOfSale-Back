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
    private String address;
    private String phone;
    private Boolean printTicket;

    public Boolean getPrintTicket() {
        return printTicket;
    }

    public Boolean setPrintTicket() {
        return printTicket;
    }

   

}