package com.zjutennis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {
    private int successCount;
    private int errorCount;
    private String message;
}
