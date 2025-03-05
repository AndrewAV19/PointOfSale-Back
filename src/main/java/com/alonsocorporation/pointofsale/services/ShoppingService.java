package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Shopping;
import java.util.List;

import com.alonsocorporation.pointofsale.dto.response.DailyExpenseDTO;
import com.alonsocorporation.pointofsale.dto.response.MonthlyExpenseDTO;
import com.alonsocorporation.pointofsale.dto.response.ShoppingDTO;
import com.alonsocorporation.pointofsale.dto.response.YearlyExpenseDTO;

public interface ShoppingService {
    List<ShoppingDTO> getAll();
    ShoppingDTO getById(Long id);
    ShoppingDTO create(Shopping shopping);
    ShoppingDTO update(Long id, Shopping shopping);
    void delete(Long id);
    DailyExpenseDTO getDailyExpense(int year, int month, int day);
    MonthlyExpenseDTO getMonthlyExpense(int year, int month);
    YearlyExpenseDTO getYearlyExpense(int year);
}