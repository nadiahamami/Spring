package com.vermeg.controllers;


import com.vermeg.entities.Project;
import com.vermeg.service.DataService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DataController {


    private final DataService dataService ;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/data/collected")
    public void saveDataJiraApi(){
        dataService.save();

    }

    @GetMapping("/data/projects")
    public List<Project> getAllProject(){
        return  dataService.getAllProject() ;
    }
}
