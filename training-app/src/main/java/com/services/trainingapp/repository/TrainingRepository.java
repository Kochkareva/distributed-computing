package com.services.trainingapp.repository;

import com.services.trainingapp.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
}
