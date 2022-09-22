package com.vermeg.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectInfo {

    private long id;
    private String displayName ;
    private String projectName ;
    private String projectKey ;
    private Ticktes ticktes;
    private String name ;
    private String key ;


}
