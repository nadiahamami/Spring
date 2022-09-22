package com.vermeg.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueDetails {


    private String key ;
    private String issueType;
    private String summary ;
    private String reporter ;
    private String creator ;
    private String assignee ;
    private String status ;
    private LocalDate created ;
    private LocalDate updated ;


    public String getString(){
        return  key       +","+
                issueType +","+
                summary +","+
                reporter +","+
                creator +","+
                assignee +","+
                status +"," +
                created +","+
                updated +",";


    }

}
