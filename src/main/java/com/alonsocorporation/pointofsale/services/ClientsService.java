package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Clients;
import java.util.List;

public interface ClientsService {
    List<Clients> getAll();
    Clients getById(Long id);
    Clients create(Clients client);
    Clients update(Long id, Clients client);
    void delete(Long id);
}