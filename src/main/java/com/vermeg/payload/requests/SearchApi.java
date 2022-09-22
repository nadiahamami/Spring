package com.vermeg.payload.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchApi {

    private String key;
    private String startDate ;
    private String endDate ;
}
