package com.services.trainingapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "t_training")
@Data
@Accessors(chain = true)
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
