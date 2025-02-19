package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Clients;
import com.alonsocorporation.pointofsale.exceptions.ClientAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.ClientNotFoundException;
import com.alonsocorporation.pointofsale.repositories.ClientsRepository;
import com.alonsocorporation.pointofsale.services.ClientsService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Clients update(Long id, Clients client) {
        return clientsRepository.findById(id)
                .map(existingClient -> {
                    existingClient.setName(client.getName());
                    existingClient.setEmail(client.getEmail());
                    existingClient.setPhone(client.getPhone());
                    existingClient.setAddress(client.getAddress());
                    existingClient.setCity(client.getCity());
                    existingClient.setState(client.getState());
                    existingClient.setZipCode(client.getZipCode());
                    existingClient.setCountry(client.getCountry());

                    return clientsRepository.save(existingClient);
                })
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        if (!clientsRepository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        clientsRepository.deleteById(id);
    }
}
