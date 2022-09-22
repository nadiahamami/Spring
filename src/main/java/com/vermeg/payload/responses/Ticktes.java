package com.vermeg.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticktes {
    private Long epic ;
    private Long story ;
    private Long task ;
    private Long bugs ;
    private Long test ;
}
