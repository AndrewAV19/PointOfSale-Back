package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class MonthlyIncomeDTO {
    private Double totalIncome;
    private Integer numberOfTransactions;
    private Double averageTicket;
    private Map<Integer, Double> incomeByDay;
    private List<SalesDTO> lastFiveTransactions;

    public MonthlyIncomeDTO(Double totalIncome, Integer numberOfTransactions, Double averageTicket, 
                           Map<Integer, Double> incomeByDay, List<SalesDTO> lastFiveTransactions) {
        this.totalIncome = totalIncome;
        this.numberOfTransactions = numberOfTransactions;
        this.averageTicket = averageTicket;
        this.incomeByDay = incomeByDay;
        this.lastFiveTransactions = lastFiveTransactions;
    }
}