package com.zjutennis.dto;

import com.zjutennis.model.Player;
import lombok.Data;

import java.util.List;

@Data
public class PlayerSearchResponse {
    private List<Player> players;
    private long totalCount;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public PlayerSearchResponse(List<Player> players, long totalCount, int currentPage, int pageSize) {
        this.players = players;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
    }
}
