package com.prox.passgpt.model;

import java.util.Map;

public class TokenErrorDetail {
    public String errorName;
    public ErrorDetail[] errorDetail;

    private TokenErrorDetail(){}

    public static TokenErrorDetail[] of(Map<String, Map<String, Integer>> errorMap){
        TokenErrorDetail[] tokenErrorDetails = new TokenErrorDetail[errorMap.size()];
        int i = 0;
        for (Map.Entry<String, Map<String, Integer>> entry : errorMap.entrySet()) {
            TokenErrorDetail tokenErrorDetail = new TokenErrorDetail();
            tokenErrorDetail.errorName = entry.getKey();
            tokenErrorDetail.errorDetail = new ErrorDetail[entry.getValue().size()];
            int j = 0;
            for (Map.Entry<String, Integer> entryDetail : entry.getValue().entrySet()) {
                ErrorDetail errorDetail = new ErrorDetail();
                errorDetail.token = entryDetail.getKey();
                errorDetail.count = entryDetail.getValue();
                tokenErrorDetail.errorDetail[j] = errorDetail;
                j++;
            }
            tokenErrorDetails[i] = tokenErrorDetail;
            i++;
        }
        return tokenErrorDetails;
    }
    public static class ErrorDetail {
        public String token;
        public int count;
    }
}
