package com.services.trainingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.trainingapp.model.Training;
import com.services.trainingapp.modelDto.ExerciseDto;
import com.services.trainingapp.modelDto.TrainingDto;
import com.services.trainingapp.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;

    @Value("${exeise-service.host}")
    private String exercise_service_host;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public List<TrainingDto> findAllTraining() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        List<TrainingDto> trainingDtos = new ArrayList<>();
        for (Training training : trainingRepository.findAll()) {
            TrainingDto trainingDto = new TrainingDto();
            trainingDto.setId(training.getId());
            trainingDto.setName(training.getName());
            trainingDto.setDescription(training.getDescription());

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://" + exercise_service_host + "/exercise/training/" + trainingDto.getId(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                List<ExerciseDto> exerciseDtos;
                try {
                    exerciseDtos = objectMapper.readValue(responseBody, ArrayList.class);
                    trainingDto.setExercises(exerciseDtos);
                } catch (JsonProcessingException e) {
                    throw new Exception("Не удалось десериализовать тело запроса в объект");
                }
            } else {
                throw new Exception("Ошибка получения объекта");
            }
            trainingDtos.add(trainingDto);
        }
        return trainingDtos;
    }

    public TrainingDto findById(int id) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        TrainingDto trainingDto = new TrainingDto();
        Training training = trainingRepository.findById(id).orElseThrow();
        trainingDto.setId(training.getId());
        trainingDto.setName(training.getName());
        trainingDto.setDescription(training.getDescription());
        ResponseEntity<String> response = restTemplate.exchange(
                "http://" + exercise_service_host + "/exercise/training/" + trainingDto.getId(), HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<ExerciseDto> exerciseDtos;
            try {
                exerciseDtos = objectMapper.readValue(responseBody, ArrayList.class);
                trainingDto.setExercises(exerciseDtos);
            } catch (JsonProcessingException e) {
                throw new Exception("Не удалось десериализовать тело запроса в объект");
            }
        } else {
            throw new Exception("Ошибка получения объекта");
        }

        return trainingDto;
    }

    public Training addTraining(TrainingDto trainingDto) {
        Training training = new Training();
        training.setName(trainingDto.getName());
        training.setDescription(trainingDto.getDescription());
        return trainingRepository.save(training);
    }

    public Training updateTraining(TrainingDto trainingDto){
        Training training = trainingRepository.findById(trainingDto.getId()).orElseThrow();
        training.setName(trainingDto.getName());
        training.setDescription(trainingDto.getDescription());
        return trainingRepository.save(training);
    }

    public void deleteTraining(int id){
        trainingRepository.delete(trainingRepository.findById(id).orElseThrow());
    }

    public void addExerciseToTraining(int exercise_id, int training_id) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
          "http://" + exercise_service_host + "/exercise/" + exercise_id, HttpMethod.GET, entity, String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            ExerciseDto exerciseDto;
            try {
                exerciseDto = objectMapper.readValue(responseBody, ExerciseDto.class);
            } catch (JsonProcessingException e) {
                throw new Exception("Не удалось десериализовать тело запроса в объект");
            }
            exerciseDto.setId_training(training_id);
            restTemplate.exchange("http://" + exercise_service_host + "/exercise/", HttpMethod.PUT, new HttpEntity<>(exerciseDto), String.class);
        } else {
            throw new Exception("Ошибка получения объекта");
        }
    }
}
