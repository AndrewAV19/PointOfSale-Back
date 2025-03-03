package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.Clients;
import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;

    public ClientDTO(Clients clients) {
        this.id = clients.getId();
        this.name = clients.getName();
    }
}

