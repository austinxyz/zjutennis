package com.zjutennis.repository;

import com.zjutennis.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // Find matches by type (singles/doubles)
    List<Match> findByMatchTypeOrderByMatchTimeDesc(String matchType);

    // Find matches by result
    List<Match> findByResultOrderByMatchTimeDesc(String result);

    // Find matches within a date range
    List<Match> findByMatchTimeBetweenOrderByMatchTimeDesc(LocalDateTime start, LocalDateTime end);

    // Find matches by tournament
    List<Match> findByTournamentNameContainingIgnoreCaseOrderByMatchTimeDesc(String tournamentName);

    // Find matches involving a specific player
    @Query("SELECT DISTINCT m FROM Match m WHERE m.player1.id = :playerId OR m.player2.id = :playerId OR m.opponentPlayer1.id = :playerId OR m.opponentPlayer2.id = :playerId ORDER BY m.matchTime DESC")
    List<Match> findByPlayerId(@Param("playerId") Long playerId);

    // Find matches where our team won
    @Query("SELECT m FROM Match m WHERE m.winnerSide = 'team1' ORDER BY m.matchTime DESC")
    List<Match> findMatchesWonByOurTeam();

    // Get recent matches
    List<Match> findTop10ByOrderByMatchTimeDesc();

    // Count matches by type
    long countByMatchType(String matchType);
}
