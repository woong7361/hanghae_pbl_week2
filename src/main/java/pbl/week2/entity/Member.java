package pbl.week2.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.entity.timeSuper.TimeStamped;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeStamped {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String pw;
    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Love> loves = new ArrayList<>();

    //=========================생성자===============================//

    private Member(String username, String pw, String nickname) {
        this.username = username;
        this.pw = pw;
        this.nickname = nickname;
    }

    public static Member createMember(String username, String pw, String nickname) {
        return new Member(username, pw, nickname);
    }

    public static Member createMemberByRegister(MemberDto.Register reg) {
        return new Member(reg.getUsername(), reg.getPw(), reg.getNickname());
    }

}
