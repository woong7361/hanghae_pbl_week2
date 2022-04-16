package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * /api/register
     */
    @PostMapping("/api/register")
    public ResultMsg register(@RequestBody MemberDto.Register registerDto) {
        memberService.register(registerDto);
        //MessageSoruce refactoring

        return new ResultMsg("success");
    }

    /**
     * 로그인 API
     * /api/login
     */
//    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody MemberDto.Login loginDto) {
        HttpHeaders header = memberService.login(loginDto);

        return new ResponseEntity<>("success", header, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {

        return "test";
    }

    @GetMapping("/api/test")
    public String apiTest() {

        return "apiTest";
    }

}
