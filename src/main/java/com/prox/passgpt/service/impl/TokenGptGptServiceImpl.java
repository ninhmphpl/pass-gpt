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
    private Consumer<Integer> countToken35;
    private Consumer<Integer> countToken35Short;
    private Consumer<Integer> countToken35Word100;

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
    public void countTokenGpt35(Consumer<Integer> countToken) {
        this.countToken35 = countToken;
    }

    @Override
    public void questionToken35(int count) {
        if (countToken35 != null) countToken35.accept(count);
    }

    @Override
    public void answerToken35(int count) {
        if (countToken35 != null) countToken35.accept(count);
    }

    @Override
    public void countTokenGpt35Short(Consumer<Integer> countToken) {
        this.countToken35Short = countToken;
    }

    @Override
    public void questionToken35Short(int count) {
        if (countToken35 != null) countToken35.accept(count);
    }

    @Override
    public void answerToken35Short(int count) {
        if (countToken35Short != null) countToken35.accept(count);
    }

    @Override
    public void countTokenGpt35Word100(Consumer<Integer> countToken) {
        this.countToken35Word100 = countToken;
    }

    @Override
    public void questionToken35Word100(int count) {
        if (countToken35Word100 != null) countToken35Word100.accept(count);
    }

    @Override
    public void answerToken35Word100(int count) {
        if (countToken35Word100 != null) countToken35Word100.accept(count);
    }
}
