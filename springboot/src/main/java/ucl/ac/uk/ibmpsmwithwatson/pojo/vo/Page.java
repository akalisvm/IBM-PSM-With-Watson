package ucl.ac.uk.ibmpsmwithwatson.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class Page {
    private List<?> records;
    private Integer total;
}
