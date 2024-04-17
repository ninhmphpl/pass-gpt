package com.prox.passgpt.service.impl;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import com.prox.passgpt.service.TokenGptService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class TokenGptGptServiceImpl implements TokenGptService {
    private Encoding encoding;
    private Consumer<Integer> countToken35Answer;
    private Consumer<Integer> countToken35Question;
    private Consumer<Integer> countToken35ShortAnswer;
    private Consumer<Integer> countToken35ShortQuestion;
    private Consumer<Integer> countToken35Word100Answer;
    private Consumer<Integer> countToken35Word100Question;

    @PostConstruct
    public void init() {
        makeEncoding();
    }

    private void makeEncoding() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        encoding = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
    }

    @Override
    public int calculateToken(String token) {
        return encoding.countTokens(token);
    }

    @Override
    public void countTokenGpt35Answer(Consumer<Integer> countToken) {
        countToken35Answer = countToken;
    }

    @Override
    public void countTokenGpt35Question(Consumer<Integer> countToken) {
        countToken35Question = countToken;
    }


    @Override
    public void questionToken35(int count) {
        if (countToken35Question != null) countToken35Question.accept(count);
    }

    @Override
    public void answerToken35(int count) {
        if (countToken35Answer != null) countToken35Answer.accept(count);
    }

    @Override
    public void countTokenGpt35ShortAnswer(Consumer<Integer> countToken) {
        countToken35ShortAnswer = countToken;
    }

    @Override
    public void countTokenGpt35ShortQuestion(Consumer<Integer> countToken) {
        countToken35ShortQuestion = countToken;
    }

    @Override
    public void questionToken35Short(int count) {
        if (countToken35ShortQuestion != null) countToken35ShortQuestion.accept(count);
    }

    @Override
    public void answerToken35Short(int count) {
        if (countToken35ShortAnswer != null) countToken35ShortAnswer.accept(count);
    }

    @Override
    public void countTokenGpt35Word100Answer(Consumer<Integer> countToken) {
        countToken35Word100Answer = countToken;
    }

    @Override
    public void countTokenGpt35Word100Question(Consumer<Integer> countToken) {
        countToken35Word100Question = countToken;
    }

    @Override
    public void questionToken35Word100(int count) {
        if (countToken35Word100Question != null) countToken35Word100Question.accept(count);
    }

    @Override
    public void answerToken35Word100(int count) {
        if (countToken35Word100Answer != null) countToken35Word100Answer.accept(count);
    }
}
