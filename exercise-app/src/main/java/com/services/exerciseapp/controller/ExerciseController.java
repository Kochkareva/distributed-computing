package com.services.exerciseapp.controller;

import com.services.exerciseapp.model.Exercise;
import com.services.exerciseapp.modelDto.ExerciseDto;
import com.services.exerciseapp.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/")
    public List<Exercise> getExercises(){
        return exerciseService.findAllExercise();
    }

    @GetMapping("/{id}")
    public Exercise getExercise(@PathVariable int id){
        return exerciseService.findById(id);
    }

    @GetMapping("/training/{id}")
    public List<Exercise> getTrainingExercises(@PathVariable int id){
        return exerciseService.findTrainingExercises(id);
    }

    @PostMapping("/")
    public Exercise createExercise(@RequestBody ExerciseDto exerciseDto){
        return exerciseService.addExercise(exerciseDto);
    }

    @PutMapping("/")
    public Exercise updateExercise(@RequestBody ExerciseDto exerciseDto){
        return exerciseService.updateExercise(exerciseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable int id){
        exerciseService.deleteExercise(id);
    }
}
