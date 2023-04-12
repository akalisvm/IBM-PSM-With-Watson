package ucl.ac.uk.ibmpsmwithwatson.pojo.po;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, String> toUserMap(User user) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("given_name", user.getGiven_name());
        map.put("family_name", user.getFamily_name());
        map.put("email", user.getEmail());
        map.put("phone_number", user.getPhone_number());
        map.put("role", user.getRole());
        map.put("gender", user.getGender());
        map.put("birthdate", user.getBirthdate());
        map.put("nhs_number", user.getNhs_number());
        map.put("doctor", user.getDoctor());
        return map;
    }
}
