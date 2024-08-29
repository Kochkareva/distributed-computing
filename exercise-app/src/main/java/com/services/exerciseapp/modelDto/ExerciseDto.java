package com.services.exerciseapp.modelDto;

import jakarta.annotation.Nullable;

public class ExerciseDto {
    private int id;
    private String name;
    private String description;
    @Nullable
    private Integer id_training;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
