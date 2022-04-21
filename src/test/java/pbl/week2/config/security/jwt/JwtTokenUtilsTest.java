package pbl.week2.config.security.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import pbl.week2.config.security.PrincipalDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static pbl.week2.config.security.jwt.JwtTokenUtils.*;

class JwtTokenUtilsTest {

    @Test
    public void tokenDecodeTest() throws Exception{
        //given
        String reqeustToken = createToken(new PrincipalDetails(1L, "member1", "1234"));
        //when
        String token = reqeustToken.replace(TOKEN_NAME_WITH_SPACE, "");
        DecodedJWT decodedJWT = verifyToken(token);
        String username = ((Claim) decodedJWT.getClaim(CLAIM_USERNAME)).asString();
        Long id = decodedJWT.getClaim(CLAIM_ID).asLong();

//        PrincipalDetails new1 = new PrincipalDetails(id, username, null);
        //then

        assertThat(reqeustToken).startsWith(TOKEN_NAME_WITH_SPACE);
//        assertThat(username).isEqualTo(new1.getUsername());
        assertThat(id).isEqualTo(1L);
        assertThat(username).isEqualTo("member1");
    }



}