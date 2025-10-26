package com.zjutennis.dto;

import lombok.Data;

@Data
public class PlayerSearchRequest {
    private String name;
    private String gender;
    private Double utrMin;
    private Double utrMax;
    private Double ntrp;
    private Double winRateMin;
    private Double winRateMax;
    private String university;
    private String city;
    private String country;

    // Pagination
    private Integer page = 1;
    private Integer pageSize = 25;

    // Sorting
    private String sortBy;
    private String sortOrder = "desc";
}
