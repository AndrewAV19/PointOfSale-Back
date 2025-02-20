package com.alonsocorporation.pointofsale.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String name) {
        super("Client with name " + name + " already exists.");
    }
}
