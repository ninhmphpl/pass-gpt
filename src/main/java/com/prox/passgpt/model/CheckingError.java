package com.prox.passgpt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CheckingError {
    private LocalDate date = LocalDate.now();
    private int gptError;
    private int gptError400;
    private int gptError429;
    private int gptError403;
    private int gptErrorTimeout;

    private int geminiError;
    private int geminiError400;
    private int geminiError429;
    private int geminiError403;
    private int geminiErrorTimeout;

    public void reset(){
        date = LocalDate.now();
        gptError = 0;
        gptError400 = 0;
        gptError429 = 0;
        gptError403 = 0;
        gptErrorTimeout = 0;
        geminiError = 0;
        geminiError400 = 0;
        geminiError429 = 0;
        geminiError403 = 0;
        geminiErrorTimeout = 0;
    }

    public void setAll(CheckingError checkingError) {
        this.date = checkingError.date;
        this.gptError = checkingError.gptError;
        this.gptError400 = checkingError.gptError400;
        this.gptError429 = checkingError.gptError429;
        this.gptError403 = checkingError.gptError403;
        this.gptErrorTimeout = checkingError.gptErrorTimeout;
        this.geminiError = checkingError.geminiError;
        this.geminiError400 = checkingError.geminiError400;
        this.geminiError429 = checkingError.geminiError429;
        this.geminiError403 = checkingError.geminiError403;
        this.geminiErrorTimeout = checkingError.geminiErrorTimeout;
    }
}
