package com.vermeg.payload.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporting {
    private String projectkey;
    private String startDate ;
    private String endDate ;
    private String status ;
}
