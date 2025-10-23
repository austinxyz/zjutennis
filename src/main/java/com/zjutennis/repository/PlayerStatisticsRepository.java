package com.zjutennis.repository;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, Long> {

    Optional<PlayerStatistics> findByPlayer(Player player);

    Optional<PlayerStatistics> findByPlayerId(Long playerId);

    List<PlayerStatistics> findByCompetitiveLevel(String competitiveLevel);

    List<PlayerStatistics> findByPlayFrequency(String playFrequency);

    List<PlayerStatistics> findByNtrpRatingGreaterThanEqual(Double ntrpRating);

    List<PlayerStatistics> findByUtrRatingBetween(Double minUtr, Double maxUtr);

    void deleteByPlayerId(Long playerId);
}
