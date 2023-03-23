package ucl.ac.uk.ibmpsmwithwatson.pojo.vo;

import lombok.Data;

@Data
public class UserVO {
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
    private String dialog;
    private String app_token;
}
