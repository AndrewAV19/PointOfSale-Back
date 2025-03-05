package com.alonsocorporation.pointofsale.dto.response;

import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
public class YearlyExpenseDTO {
    private Double totalExpense;
    private Integer numberOfTransactions;
    private Double averageTicket;
    private Map<Integer, Double> expenseByMonth;
    private List<ShoppingDTO> lastFiveTransactions;

    public YearlyExpenseDTO(Double totalExpense, Integer numberOfTransactions, Double averageTicket, 
                            Map<Integer, Double> expenseByMonth, List<ShoppingDTO> lastFiveTransactions) {
        this.totalExpense = totalExpense;
        this.numberOfTransactions = numberOfTransactions;
        this.averageTicket = averageTicket;
        this.expenseByMonth = expenseByMonth;
        this.lastFiveTransactions = lastFiveTransactions;
    }
}