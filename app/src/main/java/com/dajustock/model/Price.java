package com.dajustock.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
//Customize the Price data structure,Price data consists of Day 1 prices and Day 2 prices.
public class Price {
    private double closePrice;
    private double nextDayClosePrice;
}
