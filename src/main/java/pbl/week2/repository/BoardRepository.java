package pbl.week2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.week2.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}