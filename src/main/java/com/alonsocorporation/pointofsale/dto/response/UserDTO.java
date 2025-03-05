package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.User;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}

