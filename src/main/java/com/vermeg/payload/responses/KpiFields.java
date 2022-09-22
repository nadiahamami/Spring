package com.vermeg.payload.responses;


import com.vermeg.entities.Fields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KpiFields  implements Comparable<KpiFields> {

    private int month ;
    private Map<String, List<Fields>> fields ;

    @Override
    public int compareTo(KpiFields o) {
        return Integer.compare(getMonth(), o.getMonth());
    }
}
