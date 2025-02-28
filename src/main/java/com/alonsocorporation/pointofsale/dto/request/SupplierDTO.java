package com.alonsocorporation.pointofsale.dto.request;

import lombok.Data;

@Data
public class SupplierDTO {

    private String name;
    private String contactName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String taxId;
    private String website;
    
}
