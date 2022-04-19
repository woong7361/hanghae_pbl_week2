package pbl.week2.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import pbl.week2.entity.Member;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public final class JwtTokenUtils {

    public static final int SEC = 1000;
    public static final int MINUTE = 60 * SEC;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    public static final String TOKEN_HEADER_NAME = "Authorization";
    public static final String TOKEN_NAME_WITH_SPACE = "Bearer ";
    public static final String JWT_SECRET = "jwt_secret_!@#$%";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ID = "id";

    public static DecodedJWT verifyToken(String jwtToken) {
        try {
            return JWT
                    .require(Algorithm.HMAC512(JWT_SECRET))
                    .build()
                    .verify(jwtToken);
        } catch (AlgorithmMismatchException algorithmMismatchException){
            throw new BadCredentialsException("토큰 알고리즘 미스매칭");
        } catch (SignatureVerificationException signatureVerificationException){
            throw new BadCredentialsException("signature verifying 에러");
        } catch (TokenExpiredException tokenExpiredException) {
            throw new AccountExpiredException("토큰 만료됨");
        } catch (InvalidClaimException invalidClaimException) {
            throw new BadCredentialsException("invalidClaimError");
        }
    }

    public static String getTokenFromHeader(HttpServletRequest request) {
        return request.
                getHeader(TOKEN_HEADER_NAME).
                replace(TOKEN_NAME_WITH_SPACE, "");
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }
}
