package com.prox.passgpt.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Getter
@Setter
@Service
public class CheckingTokenCount {
    private LocalDate date = LocalDate.now();
    private long tokenAnswerGpt;
    private long tokenQuestionGpt;
    private long tokenAnswerGptShort;
    private long tokenQuestionGptShort;
    private long tokenAnswerGpt100Word;
    private long tokenQuestionGpt100Word;

    public void reset(){
        this.date = LocalDate.now();
        this.tokenAnswerGpt = 0;
        this.tokenQuestionGpt = 0;
        this.tokenAnswerGptShort = 0;
        this.tokenQuestionGptShort = 0;
        this.tokenAnswerGpt100Word = 0;
        this.tokenQuestionGpt100Word = 0;
    }

    public void setAll(CheckingTokenCount checkingTokenCount){
        this.date = checkingTokenCount.getDate();
        this.tokenAnswerGpt = checkingTokenCount.getTokenAnswerGpt();
        this.tokenQuestionGpt = checkingTokenCount.getTokenQuestionGpt();
        this.tokenAnswerGptShort = checkingTokenCount.getTokenAnswerGptShort();
        this.tokenQuestionGptShort = checkingTokenCount.getTokenQuestionGptShort();
        this.tokenAnswerGpt100Word = checkingTokenCount.getTokenAnswerGpt100Word();
        this.tokenQuestionGpt100Word = checkingTokenCount.getTokenQuestionGpt100Word();
    }
}
