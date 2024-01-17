package com.prox.passgpt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prox.passgpt.service.ApiKeyService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log4j2
public class ApiKeyServiceImpl implements ApiKeyService {
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${path.apiKey}")
    private String pathApiKey;
    private String[] apiKeys;
    private int indexCurrentKey = 0;

    @PostConstruct
    public void init() {
        try (FileReader fileReader = new FileReader(pathApiKey);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            while (line != null) {
                // Nối các dòng để có nội dung JSON hoàn chỉnh
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            apiKeys = objectMapper.readValue(stringBuilder.toString(), String[].class);

        } catch (IOException e) {
            log.error(e);
        }
    }

    @Override
    public List<String> saveApiKey(List<String> apiKeys) throws IOException {
        this.apiKeys = apiKeys.toArray(String[]::new);
        Path file = Paths.get(pathApiKey);
        if (!Files.exists(file)) {
            if(!Files.exists(file.getParent())) Files.createDirectory(file.getParent());
            Files.createFile(file);
        }
        String content = objectMapper.writeValueAsString(apiKeys);
        try (FileWriter fileWriter = new FileWriter(pathApiKey);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(content);
            log.info("Dữ liệu JSON đã được ghi vào tệp thành công." + pathApiKey);

        } catch (IOException e) {
            log.error(e);
        }
        return getApiKeys();
    }

    @Override
    public List<String> getApiKeys() {
        return List.of(apiKeys);
    }

    @Override
    public String getApiKey() {
        int indexCurrentApiKeys = this.indexCurrentKey++;
        if (apiKeys == null || apiKeys.length == 0) throw new RuntimeException("Error, apiKey is Empty");

        if (indexCurrentApiKeys < 0 || indexCurrentApiKeys >= apiKeys.length) {
            throw new RuntimeException("List apikey is null");
        }
        indexCurrentApiKeys = 0;
        return apiKeys[indexCurrentApiKeys];
    }

}
