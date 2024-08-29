package com.services.trainingapp.controller;

import com.services.trainingapp.model.Training;
import com.services.trainingapp.modelDto.TrainingDto;
import com.services.trainingapp.service.TrainingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping("/")
    public List<TrainingDto> getTrainings() throws Exception {
        return trainingService.findAllTraining();
    }

    @GetMapping("/{id}")
    public TrainingDto getTraining(@PathVariable int id) throws Exception {
        return trainingService.findById(id);
    }

    @PostMapping("/")
    public Training createTraining(@RequestBody TrainingDto trainingDto){
        return trainingService.addTraining(trainingDto);
    }

    @PutMapping("/")
    public Training updateTraining(@RequestBody TrainingDto trainingDto){
        return trainingService.updateTraining(trainingDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTraining(@PathVariable int id){
        trainingService.deleteTraining(id);
    }

    @PatchMapping("/{id_exercise}/{id_training}")
    public void addExerciseToTraining(@PathVariable int id_exercise,
                                      @PathVariable int id_training) throws Exception {
        trainingService.addExerciseToTraining(id_exercise, id_training);
    }
}
