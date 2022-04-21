package pbl.week2.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Love {

    @Id @GeneratedValue
    @Column(name = "love_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    //=========================생성자===============================//

    private Love(Member member, Board board) {
        this.member = member;
        this.board = board;
    }

    public static Love createLove(Member member, Board board) {
        Love love = new Love(member, board);
        love.getMember().getLoves().add(love);
        love.getBoard().getLoves().add(love);
        board.upLove();
        return love;
    }
}
