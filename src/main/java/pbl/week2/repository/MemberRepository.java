package pbl.week2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.MemberDto;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByUsername(String username);

    public Optional<Member> findByNickname(String nickname);
}
