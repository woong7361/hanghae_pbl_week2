package pbl.week2.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Member;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired private EntityManager em;
    @Autowired private MemberRepository memberRepository;

    private Member createMember() {
        return Member.createMember("user1", "password1", "nickname1");
    }
    private Member createMember(String username, String pw, String nickname) {
        return Member.createMember(username, pw, nickname);
    }
    private void persistenceContextclear() {
        em.flush();
        em.clear();
    }

    @Test
    public void createDeleteMemberTest() throws Exception{
        //given
        Member member1 = createMember("member1", "password1", "nickname1");
        Member member2 = createMember("member2", "password2", "nickname2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        persistenceContextclear();

        //when
        Member findMember = memberRepository.findById(member1.getId())
                .orElseThrow(() -> new PblException("log", "code"));
        memberRepository.deleteById(member2.getId());
        Optional<Member> deletedMember = memberRepository.findById(member2.getId());
        //then

        assertThat(findMember.getNickname()).isEqualTo(member1.getNickname());
        assertThatThrownBy(() -> deletedMember.orElseThrow(() -> new PblException("log", "code"))).isInstanceOf(PblException.class);
    }

    @Test
    public void findByNicknameTest() throws Exception{
        //given
        Member member = createMember();
        memberRepository.save(member);
        persistenceContextclear();
        //when
        Optional<Member> findMember = memberRepository.findByNickname(member.getNickname());
        //then
        assertThat(findMember.map(Member::getNickname).orElse(null)).isEqualTo(member.getNickname());
    }

    @Test
    public void findByUsername() throws Exception{
        //given
        Member member = createMember();
        memberRepository.save(member);
        persistenceContextclear();
        //when
        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());
        //then
        assertThat(findMember.map(Member::getUsername).orElse(null)).isEqualTo(member.getUsername());
    }

}