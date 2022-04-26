package pbl.week2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pbl.week2.entity.kakao.KakaoProfile;
import pbl.week2.entity.kakao.OAuthToken;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.service.MemberService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * /api/register
     */
    @PostMapping("/api/register")
    public ResultMsg register(@RequestBody @Valid MemberDto.Register registerDto) {
        memberService.register(registerDto);

        return new ResultMsg("success");
    }

    /**
     * 로그아웃 API
     * /api/register
     */
    @PostMapping("/api/logout")
    public String logout(@RequestHeader(value = "Authorization") String authorization) {
        return "깡통";

    }

    /**
     * 카카오 로그인 API
     */
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "5a0f454edcfa03449fa61a887a45de32");
        body.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenReqeust =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenReqeust,
                String.class
        );

        OAuthToken kakaoToken = objectMapper.readValue(response.getBody(), OAuthToken.class);


        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "Bearer " + kakaoToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> kakaoProfileReqeust =
                new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileReqeust,
                String.class
        );

        KakaoProfile kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoProfile.class);

        System.out.println("kakaoProfile = " + kakaoProfile);

        return response2.getBody();
    }
}
