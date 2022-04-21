package pbl.week2.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import pbl.week2.config.exception.CustomAuthenticationException;
import pbl.week2.config.exception.ErrorConstant;
import pbl.week2.config.security.PrincipalDetails;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

import static pbl.week2.config.exception.ErrorConstant.TOKEN_ERROR;

@Slf4j
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
            throw new CustomAuthenticationException("토큰 알고리즘 미스매칭", TOKEN_ERROR);
        } catch (SignatureVerificationException signatureVerificationException){
            throw new CustomAuthenticationException("signature verifying 에러", TOKEN_ERROR);
        } catch (TokenExpiredException tokenExpiredException) {
            throw new CustomAuthenticationException("토큰 만료됨", TOKEN_ERROR);
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomAuthenticationException("토큰 클레임 에러", TOKEN_ERROR);
        }
    }

    public static String getTokenFromHeader(HttpServletRequest request) throws CustomAuthenticationException {
        try {
            return request.
                    getHeader(TOKEN_HEADER_NAME).
                    replace(TOKEN_NAME_WITH_SPACE, "");
        } catch (Exception e) {
            throw new CustomAuthenticationException("헤더 추출 에러", TOKEN_ERROR);
        }
    }

    public static String createToken(PrincipalDetails principal) {
        String token = JWT.create()
                .withSubject("sub")
                .withExpiresAt(new Date(System.currentTimeMillis() + (2 * HOUR) ))
                .withClaim(CLAIM_ID, principal.getMemberSession().getId())
                .withClaim(CLAIM_USERNAME, principal.getUsername())
                .sign(Algorithm.HMAC512(JWT_SECRET));   //secretkey
        return TOKEN_NAME_WITH_SPACE + token;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC512(JWT_SECRET);
    }
}
