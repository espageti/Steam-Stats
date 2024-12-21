package com.example.steamstats.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // Additional query methods (if needed) can be added here
    @Query("SELECT g FROM Game g ORDER BY g.averagePlayerCount DESC NULLS LAST")
    Page<Game> findAllWithNullsLast(Pageable pageable);
}
