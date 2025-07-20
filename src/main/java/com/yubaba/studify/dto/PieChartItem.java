package com.yubaba.studify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PieChartItem {
    private String label;
    private int ratio;
    private int time;
}
