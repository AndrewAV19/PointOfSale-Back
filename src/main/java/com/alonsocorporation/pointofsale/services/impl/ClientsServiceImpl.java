package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Clients;
import com.alonsocorporation.pointofsale.exceptions.ClientAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.ClientNotFoundException;
import com.alonsocorporation.pointofsale.repositories.ClientsRepository;
import com.alonsocorporation.pointofsale.services.ClientsService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsServiceImpl implements ClientsService {

    private final ClientsRepository clientsRepository;

    public ClientsServiceImpl(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @Override
    public List<Clients> getAll() {
        return clientsRepository.findAll();
    }

    @Override
    public Clients getById(Long id) {
        return clientsRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public Clients create(Clients client) {
        // Verificar si ya existe un cliente con el mismo email
        clientsRepository.findByEmail(client.getEmail()).ifPresent(existingClient -> {
            throw new ClientAlreadyExistsException(client.getEmail());
        });

        return clientsRepository.save(client);
    }

    @Override
    public Clients update(Long id, Clients clientDetails) {
        Optional<Clients> clientOptional = clientsRepository.findById(id);
        if (clientOptional.isPresent()) {
            Clients client = clientOptional.get();

            // Actualizar solo los campos que no son null
            if (clientDetails.getName() != null) {
                client.setName(clientDetails.getName());
            }
            if (clientDetails.getEmail() != null) {
                client.setEmail(clientDetails.getEmail());
            }
            if (clientDetails.getPhone() != null) {
                client.setPhone(clientDetails.getPhone());
            }
            if (clientDetails.getAddress() != null) {
                client.setAddress(clientDetails.getAddress());
            }
            if (clientDetails.getCity() != null) {
                client.setCity(clientDetails.getCity());
            }
            if (clientDetails.getState() != null) {
                client.setState(clientDetails.getState());
            }
            if (clientDetails.getZipCode() != null) {
                client.setZipCode(clientDetails.getZipCode());
            }
            if (clientDetails.getCountry() != null) {
                client.setCountry(clientDetails.getCountry());
            }

            return clientsRepository.save(client);
        } else {
            throw new RuntimeException("Client not found with id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!clientsRepository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        clientsRepository.deleteById(id);
    }
}
