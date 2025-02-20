package com.alonsocorporation.pointofsale.exceptions;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(Long id) {
        super("Supplier with ID " + id + " not found.");
    }
}
