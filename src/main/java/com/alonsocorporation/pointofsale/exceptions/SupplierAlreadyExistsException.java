package com.alonsocorporation.pointofsale.exceptions;

public class SupplierAlreadyExistsException extends RuntimeException {
    public SupplierAlreadyExistsException(String email) {
        super("Supplier with email " + email + " already exists.");
    }
}
