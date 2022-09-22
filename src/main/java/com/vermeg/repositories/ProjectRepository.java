package com.vermeg.repositories;

import com.vermeg.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByNameContaining(String name) ;
}
