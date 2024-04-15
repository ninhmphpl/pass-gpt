package com.prox.passgpt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prox.passgpt.model.ModelChat;
import com.prox.passgpt.model.RequestBodyChat;
import com.prox.passgpt.model.ResponseChat;
import com.prox.passgpt.model.ResponseMap;
import com.prox.passgpt.service.ChatService;
import com.prox.passgpt.service.GeminiApiService;
import com.prox.passgpt.service.GptApiService;
import com.prox.passgpt.service.ModelApiService;
import com.prox.passgpt.utils.NumberUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
public class ChatServiceImpl implements ChatService, ModelApiService {
    private final Path PATH_MODEL_PERCENT = Paths.get("/home/server/model_percent.json");

    @Autowired
    private GptApiService gptApiService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GeminiApiService geminiApiService;
    private final Map<ModelChat, Function<RequestBodyChat, Flux<ResponseChat>>> chatServiceMap = new HashMap<>();
    private final Map<ModelChat, Integer> modelPercent = new HashMap<>();

    @PostConstruct
    public void init() {
        chatServiceMap.put(ModelChat.gpt35, this::callGpt35);
        chatServiceMap.put(ModelChat.gpt35_shot, this::callGpt35ShortAnswer);
        chatServiceMap.put(ModelChat.gpt35_100word, this::callGpt35Answer100Word);
        chatServiceMap.put(ModelChat.gpt4, this::callGpt40);
        chatServiceMap.put(ModelChat.gemini, this::callGemini);

        if (!Files.exists(PATH_MODEL_PERCENT)) {
            try {
                Files.createFile(PATH_MODEL_PERCENT);
                modelPercent.put(ModelChat.gpt35, 0);
                modelPercent.put(ModelChat.gpt35_shot, 0);
                modelPercent.put(ModelChat.gpt35_100word, 0);
                modelPercent.put(ModelChat.gpt4, 0);
                modelPercent.put(ModelChat.gemini, 0);
                modelPercent.forEach((model, integer) -> modelPercent.put(model, 100 / modelPercent.size()));
                objectMapper.writeValue(PATH_MODEL_PERCENT.toFile(), new ResponseMap(modelPercent));
            } catch (Exception e) {
                log.error("Error create file " + PATH_MODEL_PERCENT, e);
            }
        } else {
            try {
                modelPercent.putAll(objectMapper.readValue(PATH_MODEL_PERCENT.toFile(), ResponseMap.class).toMap());
            } catch (Exception e) {
                log.error("Error read file " + PATH_MODEL_PERCENT, e);
            }
        }

        testGetModel();
    }

    public ModelChat getModel() {
        int total = modelPercent.values().stream().mapToInt(Integer::intValue).sum();
        int randomNumber = NumberUtils.randomInt(0, total);

        int sum = 0;
        for (Map.Entry<ModelChat, Integer> entry : modelPercent.entrySet()) {
            sum += entry.getValue();
            if (randomNumber <= sum) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Model not found");
    }

    private void testGetModel(){
        Map<ModelChat, Integer> modelPercent = new HashMap<>();
        for (int i = 0; i < 100; i++){
            ModelChat model = getModel();
            modelPercent.put(model, modelPercent.getOrDefault(model, 0) + 1);
        }
        log.info("Test get model: " + modelPercent);
    }

    @Override
    public Flux<ResponseChat> callApi(RequestBodyChat requestBodyChat) {
        return chatServiceMap.get(getModel()).apply(requestBodyChat);
    }

    private Flux<ResponseChat> callGpt35(RequestBodyChat requestBodyChat) {
        return gptApiService.makeStreamRequest(requestBodyChat, ModelChat.gpt35);
    }

    private Flux<ResponseChat> callGpt35ShortAnswer(RequestBodyChat requestBodyChat) {
        return gptApiService.makeStreamRequest(requestBodyChat, ModelChat.gpt35_shot);
    }

    private Flux<ResponseChat> callGpt35Answer100Word(RequestBodyChat requestBodyChat) {
        return gptApiService.makeStreamRequest(requestBodyChat, ModelChat.gpt35_100word);
    }

    private Flux<ResponseChat> callGpt40(RequestBodyChat requestBodyChat) {
        return gptApiService.makeStreamRequest(requestBodyChat, ModelChat.gpt4);
    }

    private Flux<ResponseChat> callGemini(RequestBodyChat requestBodyChat) {
        return null;
    }

    @Override
    public Map<ModelChat, Integer> getModelPercent() {
        return modelPercent;
    }

    @Override
    public Map<ModelChat, Integer> setModelPercent(Map<ModelChat, Integer> modelPercent) {
        formatPercent(modelPercent);
        this.modelPercent.clear();
        this.modelPercent.putAll(modelPercent);
        try {
            objectMapper.writeValue(PATH_MODEL_PERCENT.toFile(), new ResponseMap(this.modelPercent));
        } catch (Exception e) {
            log.error("Error write file " + PATH_MODEL_PERCENT, e);
        }
        return this.modelPercent;
    }

    private void formatPercent(Map<ModelChat, Integer> modelPercent){
        int total = modelPercent.values().stream().mapToInt(Integer::intValue).sum();
        if (total != 100){
            modelPercent.forEach((model, integer) -> modelPercent.put(model,(int)((float)integer / total * 100)));
        }
        int total2 = modelPercent.values().stream().mapToInt(Integer::intValue).sum();
        int remnants = 100 - total2;
        if (remnants > 0){
            modelPercent.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(entry -> modelPercent.put(entry.getKey(), entry.getValue() + remnants));
        }
    }

}
