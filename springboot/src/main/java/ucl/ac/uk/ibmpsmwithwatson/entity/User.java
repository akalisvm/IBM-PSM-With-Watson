package ucl.ac.uk.ibmpsmwithwatson.entity;

import lombok.Data;

@Data
public class User {
    private String id;
    private String given_name;
    private String family_name;
    private String email;
    private String phone_number;
    private String password;
    private String role;
    private String gender;
    private String birthdate;
    private String nhs_number;
    private String doctor;
    private String questionnaire;
    private String app_token;
}
