package com.vermeg.payload.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    private String accountId ;
    private String emailAddress ;
    private String displayName ;
    private  boolean active ;
}
