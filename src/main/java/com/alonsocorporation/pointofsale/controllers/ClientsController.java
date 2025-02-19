package com.alonsocorporation.pointofsale.controllers;

import com.alonsocorporation.pointofsale.entities.Clients;
import com.alonsocorporation.pointofsale.services.ClientsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    private final ClientsService clientsService;

    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @GetMapping
    public ResponseEntity<List<Clients>> getAllClients() {
        List<Clients> clients = clientsService.getAll();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clients> getClientById(@PathVariable Long id) {
        Clients client = clientsService.getById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<Clients> createClient(@RequestBody Clients client) {
        Clients newClient = clientsService.create(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clients> updateClient(@PathVariable Long id, @RequestBody Clients client) {
        Clients updatedClient = clientsService.update(id, client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
