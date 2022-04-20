package pbl.week2.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pbl.week2.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.member order by b.likeCount desc")
    Slice<Board> findAllByOrderByLikeCountDesc(Pageable pageable);

    @Query("select b from Board b join fetch b.member")
    List<Board> findAllWithMember();


}
