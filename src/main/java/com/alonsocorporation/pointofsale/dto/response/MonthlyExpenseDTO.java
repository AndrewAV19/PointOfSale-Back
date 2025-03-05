package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class MonthlyExpenseDTO {
    private Double totalExpense;
    private Integer numberOfTransactions;
    private Double averageTicket;
    private Map<Integer, Double> expenseByDay;
    private List<ShoppingDTO> lastFiveTransactions;

    public MonthlyExpenseDTO(Double totalExpense, Integer numberOfTransactions, Double averageTicket, 
                            Map<Integer, Double> expenseByDay, List<ShoppingDTO> lastFiveTransactions) {
        this.totalExpense = totalExpense;
        this.numberOfTransactions = numberOfTransactions;
        this.averageTicket = averageTicket;
        this.expenseByDay = expenseByDay;
        this.lastFiveTransactions = lastFiveTransactions;
    }
}