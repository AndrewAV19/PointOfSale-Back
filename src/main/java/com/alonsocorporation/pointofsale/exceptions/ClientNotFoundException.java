package com.alonsocorporation.pointofsale.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Client with ID " + id + " not found.");
    }
}
