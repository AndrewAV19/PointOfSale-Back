package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class YearlyIncomeDTO {
    private Double totalIncome;
    private Integer numberOfTransactions;
    private Double averageTicket;
    private Map<Integer, Double> incomeByMonth;
    private List<SalesDTO> lastFiveTransactions;

    public YearlyIncomeDTO(Double totalIncome, Integer numberOfTransactions, Double averageTicket, 
                          Map<Integer, Double> incomeByMonth, List<SalesDTO> lastFiveTransactions) {
        this.totalIncome = totalIncome;
        this.numberOfTransactions = numberOfTransactions;
        this.averageTicket = averageTicket;
        this.incomeByMonth = incomeByMonth;
        this.lastFiveTransactions = lastFiveTransactions;
    }
}