package pbl.week2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pbl.week2.entity.Love;

public interface LoveRepository extends JpaRepository<Love, Long> {
}
