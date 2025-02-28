package com.alonsocorporation.pointofsale.dto.response;

import com.alonsocorporation.pointofsale.entities.Categories;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO(Categories category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}

