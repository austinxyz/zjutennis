package com.zjutennis.repository;

import com.zjutennis.model.PlayerAlumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerAlumniRepository extends JpaRepository<PlayerAlumni, Long> {
}
