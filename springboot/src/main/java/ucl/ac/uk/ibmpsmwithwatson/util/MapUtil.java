package ucl.ac.uk.ibmpsmwithwatson.util;

import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
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
