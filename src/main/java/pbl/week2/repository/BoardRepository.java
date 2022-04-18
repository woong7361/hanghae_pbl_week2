package pbl.week2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pbl.week2.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
