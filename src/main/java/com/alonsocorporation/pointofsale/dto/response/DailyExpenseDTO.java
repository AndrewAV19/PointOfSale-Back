package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DailyExpenseDTO {
    private Double totalExpense;
    private Integer numberOfTransactions;
    private Double averageTicket;
    private Map<Integer, Double> expenseByHour;
    private List<ShoppingDTO> lastFiveTransactions;

    public DailyExpenseDTO(Double totalExpense, Integer numberOfTransactions, Double averageTicket, 
                           Map<Integer, Double> expenseByHour, List<ShoppingDTO> lastFiveTransactions) {
        this.totalExpense = totalExpense;
        this.numberOfTransactions = numberOfTransactions;
        this.averageTicket = averageTicket;
        this.expenseByHour = expenseByHour;
        this.lastFiveTransactions = lastFiveTransactions;
    }
}