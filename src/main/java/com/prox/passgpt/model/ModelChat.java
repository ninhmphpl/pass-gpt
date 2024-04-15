package com.prox.passgpt.model;

public enum ModelChat {
    gemini("gemini"),
    gpt35 ("gpt35"),
    gpt35_shot("gpt35_shot"),
    gpt35_100word ("gpt35_100word"),
    gpt4("gpt4");

    public final String nameModel;

    ModelChat(String nameModel){
        this.nameModel = nameModel;
    }

    @Override
    public String toString() {
        return this.nameModel;
    }

    public static ModelChat valueOfString(String name){
        for (ModelChat model : ModelChat.values()){
            if (model.nameModel.equals(name)){
                return model;
            }
        }
        return null;
    }
}