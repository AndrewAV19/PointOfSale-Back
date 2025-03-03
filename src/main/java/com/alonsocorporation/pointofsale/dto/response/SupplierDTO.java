package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.Suppliers;
import lombok.Data;

@Data
public class SupplierDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public SupplierDTO(Suppliers supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.email = supplier.getEmail();
        this.phone = supplier.getPhone();
    }
}
