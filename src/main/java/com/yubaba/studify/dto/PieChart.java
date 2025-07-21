package com.yubaba.studify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PieChart {
    private List<PieChartItem> items;
}