package com.services.exerciseapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "t_exercise")
@Data
@Accessors(chain = true)
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer id_training;

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

    public Integer getId_training() {
        return id_training;
    }

    public void setId_training(Integer id_training) {
        this.id_training = id_training;
    }
}
