package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.config.security.PrincipalDetails;
import pbl.week2.service.LoveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoveController {

    private final LoveService loveService;


    /**
     * 좋아요 추가
     */
    @PostMapping("/posts/{postId}/like")
    public ResultMsg createLove(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        loveService.createLove(postId, principalDetails.getMemberSession().getId());
        return new ResultMsg("success");
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping("/post/{postId}/like")
    public ResultMsg deleteLove(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        loveService.deleteLove(postId, principalDetails.getMemberSession().getId());

        return new ResultMsg("success");
    }
}
