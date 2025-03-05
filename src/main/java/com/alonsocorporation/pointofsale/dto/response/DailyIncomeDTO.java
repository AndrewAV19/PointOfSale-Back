package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DailyIncomeDTO {
    private Double totalIncome;
    private Integer numberOfTransactions;
    private Double averageTicket;
    private Map<Integer, Double> incomeByHour;
    private List<SalesDTO> lastFiveTransactions;

    public DailyIncomeDTO(Double totalIncome, Integer numberOfTransactions, Double averageTicket, 
                         Map<Integer, Double> incomeByHour, List<SalesDTO> lastFiveTransactions) {
        this.totalIncome = totalIncome;
        this.numberOfTransactions = numberOfTransactions;
        this.averageTicket = averageTicket;
        this.incomeByHour = incomeByHour;
        this.lastFiveTransactions = lastFiveTransactions;
    }
}