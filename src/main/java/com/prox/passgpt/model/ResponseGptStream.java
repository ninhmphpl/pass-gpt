package com.prox.passgpt.model;

import java.util.List;

public class ResponseGptStream {
    public String id;
    public String object;
    public long created;
    public String model;
    public String system_fingerprint;
    public List<Choice> choices;

    public static class Choice {
        public int index;
        public Delta delta;
        public Object logprobs;
        public Object finish_reason;
    }

    public static class Delta {
        public String content;
    }
}
