package ucl.ac.uk.ibmpsmwithwatson.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String sub;
    private String iss;
    private String aud;
    private String family_name;
    private String given_name;
    private String email;
    private String phone_number;
    private String birthdate;
    private String address;
    private String nhs_number;
}
