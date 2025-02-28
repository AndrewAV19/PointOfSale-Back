package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.Suppliers;
import lombok.Data;

@Data
public class SupplierDTO {
    private Long id;
    private String name;

    public SupplierDTO(Suppliers supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
    }
}
