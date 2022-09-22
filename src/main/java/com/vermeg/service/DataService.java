package com.vermeg.service;

import com.vermeg.entities.Project;

import java.util.List;

public interface DataService {

    void save() ;
    List<Project> getAllProject();
}
