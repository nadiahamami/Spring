package com.vermeg.entities;

import com.vermeg.payload.responses.Ticktes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private String displayName ;
    private String projectName ;
    private String projectKey ;
    private String name ;
}
