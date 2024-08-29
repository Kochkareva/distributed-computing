package com.services.exerciseapp.service;

import com.services.exerciseapp.model.Exercise;
import com.services.exerciseapp.modelDto.ExerciseDto;
import com.services.exerciseapp.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<Exercise> findAllExercise(){
        return exerciseRepository.findAll();
    }

    public Exercise findById(int id){
        return exerciseRepository.findById(id).orElseThrow();
    }

    public List<Exercise> findTrainingExercises(int training_id){
        return exerciseRepository.findAll().stream()
                .filter(exercise -> exercise.getId_training() != null)
                .filter(exercise -> exercise.getId_training() == training_id)
                .toList();
    }

    public Exercise addExercise(ExerciseDto exerciseDto) {
        Exercise exercise = new Exercise();
        exercise.setName(exerciseDto.getName());
        exercise.setDescription(exerciseDto.getDescription());
        if (exerciseDto.getId_training() != null) {
            exercise.setId_training(exerciseDto.getId_training());
        }
        return exerciseRepository.save(exercise);
    }

    public Exercise updateExercise(ExerciseDto exerciseDto){
        Exercise exercise = exerciseRepository.findById(exerciseDto.getId()).orElseThrow();
        exercise.setName(exerciseDto.getName());
        exercise.setDescription(exerciseDto.getDescription());
        if (exerciseDto.getId_training() != null) {
            exercise.setId_training(exerciseDto.getId_training());
        }
        return exerciseRepository.save(exercise);
    }

    public void deleteExercise(int id){
        exerciseRepository.delete(exerciseRepository.findById(id).orElseThrow());
    }
}
