package com.prox.passgpt.controller;

import com.prox.passgpt.model.ResponseMap;
import com.prox.passgpt.service.ModelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping ("/api/v1/model")
public class ModelApi {
    @Autowired
    private ModelApiService modelService;

    @GetMapping
    public ResponseEntity<ResponseMap> getModel(){
        ResponseMap responseMap = new ResponseMap(modelService.getModelPercent());
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping
    public ResponseEntity<ResponseMap> getModel(@RequestBody ResponseMap model){
        return ResponseEntity.ok(new ResponseMap(modelService.setModelPercent(model.toMap())));
    }
}
