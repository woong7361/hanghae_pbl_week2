package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.config.security.PrincipalDetails;
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
        //MessageSoruce refactoring

        return new ResultMsg("success");
    }

    /**
     * 로그아웃 API
     * /api/register
     */
    @PostMapping("/api/logout")
    public String logout(@RequestHeader(value="Authorization")String authorization) {
        return "깡통";

    }


}
