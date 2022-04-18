package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.security.PrincipalDetails;
import pbl.week2.service.BoardService;

import javax.validation.Valid;
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
    public BoardDto.PostIdRes createBoard(
            @Valid @RequestBody BoardDto.CreateReq createReq,
            @AuthenticationPrincipal PrincipalDetails principal) {
        Long boardId = boardService.createBoard(createReq, principal.getMemberSession().getId());
        return new BoardDto.PostIdRes(1L);
    }

    /**
     * BoardList 전부 조회
     * 인증없이 API도달 가능이므로 princiaplDetails null checking필요
     */
    @GetMapping("/posts")
    public List<BoardDto.PostRes> getBoardList(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails != null ? principalDetails.getMemberSession().getId() : null;
        return boardService.getBoardList(memberId);
    }

    /**
     * Board 조회
     * 인증없이 API도달 가능이므로 princiaplDetails null checking필요
     */
    @GetMapping("/posts/{postId}")
    public BoardDto.PostRes getBoard(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails != null ? principalDetails.getMemberSession().getId() : null;
        return boardService.getBoard(postId, memberId);
    }

    /**
     * Board 수정
     */
    @PatchMapping("/posts/{postId}")
    public ResultMsg patchBoard(
            @PathVariable("postId") Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody BoardDto.CreateReq patchReq
    ) {

        boardService.patchBoard(boardId, principalDetails.getMemberSession().getId(), patchReq);
        return new ResultMsg("success");
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
