package ucl.ac.uk.ibmpsmwithwatson.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String sign = "ibm-psm-with-watson";

    public static String getToken(Map<String, String> map) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.HOUR, 24);
        Date expiresDate = nowTime.getTime();

        JWTCreator.Builder builder = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate);

        map.forEach(builder::withClaim);

        return builder.sign(Algorithm.HMAC256(sign));
    }

    public static boolean verifyToken(String token)  {
        return JWT.require(Algorithm.HMAC256(sign)).build().verify(token) != null;
    }

}