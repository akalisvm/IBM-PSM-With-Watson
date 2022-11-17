package ucl.ac.uk.ibmpsmwithwatson.utils;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ucl.ac.uk.ibmpsmwithwatson.entity.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        // get the token from the header with the key "token"
        String token = request.getHeader("token");
        Result<?> result;

        try {
            // verify the token
            return JwtUtils.verifyToken(token);
        } catch (SignatureVerificationException e) {
            result = Result.error("10010", "Signature Verification failed");
        } catch (TokenExpiredException e) {
            result = Result.error("10011", "Token Expired");
        } catch (AlgorithmMismatchException e) {
            result = Result.error("10012", "Token Algorithm Mismatch");
        } catch (Exception e) {
            result = Result.error("10013", "No Token Included");
        }

        // response by json
        String json = new ObjectMapper().writeValueAsString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}