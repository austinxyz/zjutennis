package com.zjutennis.repository;

import com.zjutennis.model.MatchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {

    // Find all players in a match
    List<MatchPlayer> findByMatchIdOrderByTeamAscPositionAsc(Long matchId);

    // Find all matches for a player
    List<MatchPlayer> findByPlayerIdOrderByIdDesc(Long playerId);

    // Find players by team in a match
    List<MatchPlayer> findByMatchIdAndTeamOrderByPositionAsc(Long matchId, String team);

    // Find our team players in a match
    List<MatchPlayer> findByMatchIdAndIsOurTeamTrue(Long matchId);

    // Find opponent players in a match
    List<MatchPlayer> findByMatchIdAndIsOurTeamFalse(Long matchId);

    // Check if a player is in a specific match
    boolean existsByMatchIdAndPlayerId(Long matchId, Long playerId);

    // Delete all players in a match
    void deleteByMatchId(Long matchId);
}
