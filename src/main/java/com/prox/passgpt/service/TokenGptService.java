package com.prox.passgpt.service;

import java.util.function.Consumer;

public interface TokenGptService {

    int calculateToken(String token);

    void countTokenGpt35Answer(Consumer<Integer> countToken);
    void countTokenGpt35Question(Consumer<Integer> countToken);
    void questionToken35(int count);
    void answerToken35(int count);

    void countTokenGpt35ShortAnswer(Consumer<Integer> countToken);
    void countTokenGpt35ShortQuestion(Consumer<Integer> countToken);
    void questionToken35Short(int count);
    void answerToken35Short(int count);

    void countTokenGpt35Word100Answer(Consumer<Integer> countToken);
    void countTokenGpt35Word100Question(Consumer<Integer> countToken);
    void questionToken35Word100(int count);
    void answerToken35Word100(int count);


}
