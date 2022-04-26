package pbl.week2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pbl.week2.entity.Love;

import java.util.List;
import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {


    //
    @Query("select l from Love l where l.member.id = :memberId")
    List<Love> findByMemberIdWithBoard(@Param("memberId") Long memberId);



    //
    @Query("select l from Love l where l.member.id = :memberId and l.board.id = :boardId")
    Optional<Love> isLoveByMember(@Param("memberId") Long memberId, @Param("boardId") Long boardId);
}
