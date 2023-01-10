package ucl.ac.uk.ibmpsmwithwatson.entity;

import lombok.Data;

import java.util.List;

@Data
public class Questionnaire {
    private List<String> questions;
    private Integer size;
}
