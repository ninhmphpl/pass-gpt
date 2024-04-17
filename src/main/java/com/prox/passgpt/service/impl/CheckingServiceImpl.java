package com.prox.passgpt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prox.passgpt.model.CheckingError;
import com.prox.passgpt.model.CheckingTokenCount;
import com.prox.passgpt.service.CheckingService;
import com.prox.passgpt.service.TokenGptService;
import com.prox.passgpt.service.errorchat.GeminiErrorService;
import com.prox.passgpt.service.errorchat.GptErrorService;
import com.prox.passgpt.utils.TimeTool;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class CheckingServiceImpl implements CheckingService {
    @Value("${path.save.counttoken}")
    private String pathSaveCountToken;
    @Value("${path.save.error}")
    private String pathSaveError;

    private final Map<String, Map<String, Integer>> errorMap = new HashMap<>();

    @Autowired
    private TokenGptService tokenGptService;
    @Autowired
    private GeminiErrorService geminiErrorService;
    @Autowired
    private GptErrorService gptErrorService;
    @Autowired
    private ObjectMapper objectMapper;

    private final CheckingTokenCount checkingTokenCount = new CheckingTokenCount();
    private final CheckingError checkingError = new CheckingError();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init() {
        getErrorEvent();
        getTokenCountEvent();
        LocalTime timeFixed = LocalTime.of(0, 0, 0);
        long timeWait = TimeTool.calculatorMilliSecondsToTimeOfDay(timeFixed);
        scheduledExecutorService.scheduleAtFixedRate(this::updateDataToday, timeWait, TimeTool.TIME_DAY, TimeUnit.MILLISECONDS);
        updateDataTodayExist();
    }

    private void updateDataTodayExist() {
        try {
            CheckingTokenCount checkingTokenCount = objectMapper.readValue(new File(pathSaveCountToken + "/" + LocalDate.now() + ".json"), CheckingTokenCount.class);
            this.checkingTokenCount.setAll(checkingTokenCount);
        } catch (Exception e) {
            log.error(e);
        }
        try {
            CheckingError checkingError = objectMapper.readValue(new File(pathSaveError + "/" + LocalDate.now() + ".json"), CheckingError.class);
            this.checkingError.setAll(checkingError);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void updateDataToday() {
        saveCheckingTokenCount();
        saveCheckingError();
        checkingTokenCount.reset();
        checkingError.reset();
        errorMap.clear();
    }

    private void getErrorEvent() {
        geminiErrorService.error(s -> {
            synchronized (errorMap) {
                String key = "error gemini";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGeminiError(checkingError.getGeminiError() + 1);
            }
        });
        geminiErrorService.error400(s -> {
            synchronized (errorMap) {
                String key = "error400 gemini";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGeminiError400(checkingError.getGeminiError400() + 1);
            }
        });
        geminiErrorService.error429(s -> {
            synchronized (errorMap) {
                String key = "error429 gemini";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGeminiError429(checkingError.getGeminiError429() + 1);
            }
        });
        geminiErrorService.error403(s -> {
            synchronized (errorMap) {
                String key = "error403 gemini";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGeminiError403(checkingError.getGeminiError403() + 1);
            }
        });
        geminiErrorService.errorTimeOut(() -> {
            synchronized (errorMap) {
                String key = "error429 gemini";
                String token = "all";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(token, map.getOrDefault(token, 0) + 1);
                checkingError.setGeminiErrorTimeout(checkingError.getGeminiErrorTimeout() + 1);
            }
        });
        gptErrorService.error(s -> {
            synchronized (errorMap) {
                String key = "error gpt";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGptError(checkingError.getGptError() + 1);
            }
        });
        gptErrorService.error400(s -> {
            synchronized (errorMap) {
                String key = "error400 gpt";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGptError400(checkingError.getGptError400() + 1);
            }
        });
        gptErrorService.error429(s -> {
            synchronized (errorMap) {
                String key = "error429 gpt";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGptError429(checkingError.getGptError429() + 1);
            }
        });
        gptErrorService.error403(s -> {
            synchronized (errorMap) {
                String key = "error403 gpt";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(s, map.getOrDefault(s, 0) + 1);
                checkingError.setGptError403(checkingError.getGptError403() + 1);
            }
        });
        gptErrorService.errorTimeOut(() -> {
            synchronized (errorMap) {
                String key = "error429 gpt";
                String token = "all";
                Map<String, Integer> map = errorMap.computeIfAbsent(key, k -> new HashMap<>());
                map.put(token, map.getOrDefault(token, 0) + 1);
                checkingError.setGptErrorTimeout(checkingError.getGptErrorTimeout() + 1);
            }
        });
    }

    private void getTokenCountEvent() {
        tokenGptService.countTokenGpt35Answer(count -> {
            checkingTokenCount.setTokenAnswerGpt(checkingTokenCount.getTokenAnswerGpt() + count);
        });
        tokenGptService.countTokenGpt35Question(count -> {
            checkingTokenCount.setTokenQuestionGpt(checkingTokenCount.getTokenQuestionGpt() + count);
        });
        tokenGptService.countTokenGpt35ShortAnswer(count -> {
            checkingTokenCount.setTokenAnswerGptShort(checkingTokenCount.getTokenAnswerGptShort() + count);
        });
        tokenGptService.countTokenGpt35ShortQuestion(count -> {
            checkingTokenCount.setTokenQuestionGptShort(checkingTokenCount.getTokenQuestionGptShort() + count);
        });
        tokenGptService.countTokenGpt35Word100Answer(count -> {
            checkingTokenCount.setTokenAnswerGpt100Word(checkingTokenCount.getTokenAnswerGpt100Word() + count);
        });
        tokenGptService.countTokenGpt35Word100Question(count -> {
            checkingTokenCount.setTokenQuestionGpt100Word(checkingTokenCount.getTokenQuestionGpt100Word() + count);
        });
    }

    private void saveCheckingTokenCount() {
        try {
            File file = new File(pathSaveCountToken + "/" + checkingTokenCount.getDate() + ".json");
            if (!Files.exists(Paths.get(file.getParent()))) {
                Files.createDirectories(Paths.get(file.getParent()));
            }
            objectMapper.writeValue(file, checkingTokenCount);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void saveCheckingError() {
        try {
            File file = new File(pathSaveError + "/" + checkingError.getDate() + ".json");
            if (!Files.exists(Paths.get(file.getParent()))) {
                Files.createDirectories(Paths.get(file.getParent()));
            }
            objectMapper.writeValue(file, checkingError);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public List<CheckingTokenCount> getCheckingTokenCount() {
        saveCheckingTokenCount();
        List<CheckingTokenCount> checkingTokenCounts = new ArrayList<>();
        File[] files = getAllFile(pathSaveCountToken);
        for (File file : files) {
            try {
                String content = Files.readString(file.toPath());
                CheckingTokenCount checkingTokenCount = objectMapper.readValue(content, CheckingTokenCount.class);
                checkingTokenCounts.add(checkingTokenCount);
            } catch (Exception e) {
                log.error(e);
            }
        }
        return checkingTokenCounts;
    }


    @Override
    public List<CheckingError> getCheckingError() {
        saveCheckingError();
        List<CheckingError> checkingErrors = new ArrayList<>();
        File[] files = getAllFile(pathSaveError);
        for (File file : files) {
            try {
                String content = Files.readString(file.toPath());
                CheckingError checkingError = objectMapper.readValue(content, CheckingError.class);
                checkingErrors.add(checkingError);
            } catch (Exception e) {
                log.error(e);
            }
        }
        return checkingErrors;
    }

    @Override
    public Map<String, Map<String, Integer>> getErrorMapDetail() {
        Map<String, Map<String, Integer>> errorMap = new HashMap<>(this.errorMap);
        this.errorMap.clear();
        return errorMap;
    }

    private File[] getAllFile(String path) {
        File file = new File(path);
        return file.listFiles();
    }
}
