package pbl.week2.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.entity.timeSuper.TimeStamped;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends TimeStamped {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String content;
    private String picture;
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Love> loves = new ArrayList<>();

    //=========================생성자===============================//
    public Board(String content, String picture, Member member) {
        this.content = content;
        this.picture = picture;
        this.likeCount = 0L;
        this.member = member;
    }

    //=========================편의 메소드===============================//
    public static Board createBoard(String content, String picture, Member member) {
        Board board = new Board(content, picture, member);
        board.member.getBoards().add(board);
        return board;
    }

    //=========================비즈니스 로직===============================//

    public void patch(String content, String filePath) {
        this.content = content;
        this.picture = filePath;
    }

    public void upLove() {
        this.likeCount += 1;
    }

    public void downLove() {
        this.likeCount -= 1;
    }
}
