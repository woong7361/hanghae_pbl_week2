package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.config.security.PrincipalDetails;
import pbl.week2.service.BoardService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    /**
     * Board 추가 API
     */
    @PostMapping("/posts")
    public BoardDto.PostRes createBoard(
            BoardDto.FileReq fileReq,
            @AuthenticationPrincipal PrincipalDetails principal) throws Exception {

        return boardService.createBoard(fileReq, principal.getMemberSession().getId());

    }

    /**
     * BoardList 전부 조회
     * 인증없이 API도달 가능이므로 princiaplDetails null checking필요
     */
    @GetMapping(value = "/posts")
    public BoardDto.PostResList getBoardList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {
        Long memberId = principalDetails != null ? principalDetails.getMemberSession().getId() : null;
        return boardService.getBoardList(memberId, pageable);
    }

    /**
     * Board 조회
     * 인증없이 API도달 가능이므로 princiaplDetails null checking필요
     */
    @GetMapping(value = "/posts/{postId}")
    public BoardDto.PostRes getBoard(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        Long memberId = principalDetails != null ? principalDetails.getMemberSession().getId() : null;
        return boardService.getBoard(postId, memberId);
    }

    /**
     * Board 수정
     */
    @PatchMapping("/posts/{postId}")
    public BoardDto.PostRes patchBoard(
            @PathVariable("postId") Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            BoardDto.FileReq fileReq) {
        return boardService.patchBoard(boardId, principalDetails.getMemberSession().getId(), fileReq);

    }

    /**
     * Board 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResultMsg patchBoard(
            @PathVariable("postId") Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        boardService.removeBoard(boardId, principalDetails.getMemberSession().getId());
        return new ResultMsg("success");
    }
}
