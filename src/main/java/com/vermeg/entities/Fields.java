package com.vermeg.entities;


import com.vermeg.payload.responses.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fields  {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String fiedlsKey ;
    private  String summary;

    @ManyToOne
    private IssueType issuetype ;

    @ManyToOne
    private Project project ;

    @ManyToOne
    private Status status ;

    private LocalDate created ;
    private LocalDate updated ;

    @ManyToOne
    private User reporter ;
    @ManyToOne
    private User creator ;
    @ManyToOne
    private User assignee ;


}
