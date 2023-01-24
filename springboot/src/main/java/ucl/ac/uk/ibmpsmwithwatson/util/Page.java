package ucl.ac.uk.ibmpsmwithwatson.util;

import lombok.Data;

import java.util.List;

@Data
public class Page {
    private List<?> rows;
    private Integer total;
}
