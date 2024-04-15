package com.prox.passgpt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
@NoArgsConstructor
public class ResponseMap {
    public List<Entry> entries;

    public ResponseMap(Map<ModelChat, Integer> map){
        entries = new ArrayList<>();
        for (ModelChat key  : map.keySet()){
            String keyResult = key.nameModel;
            entries.add(new Entry(keyResult, map.get(key)));
        }
        entries.sort(Comparator.comparing((Entry o) -> o.value).reversed());
    }
    public Map<ModelChat, Integer> toMap(){
        Map<ModelChat, Integer> map = new java.util.HashMap<>();
        for (Entry entry : entries){
            map.put(ModelChat.valueOfString(entry.key), entry.value);
        }
        return map;
    }
    @Getter
    public static class Entry{
        public String key;
        public int value;

        public Entry(String key, int value){
            this.key = key;
            this.value = value;
        }
    }
}
