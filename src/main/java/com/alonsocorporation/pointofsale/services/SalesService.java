package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.Sales;
import java.util.List;
import com.alonsocorporation.pointofsale.dto.response.ClientDebtDTO;
import com.alonsocorporation.pointofsale.dto.response.DailyIncomeDTO;
import com.alonsocorporation.pointofsale.dto.response.MonthlyIncomeDTO;
import com.alonsocorporation.pointofsale.dto.response.SalesDTO;
import com.alonsocorporation.pointofsale.dto.response.YearlyIncomeDTO;

public interface SalesService {
    List<SalesDTO> getAll();
    SalesDTO getById(Long id);
    SalesDTO create(Sales sales);
    SalesDTO update(Long id, Sales sales);
    void delete(Long id);
    void cancel(Long id);
    List<SalesDTO> getSalesByState(String state);
    List<ClientDebtDTO> getClientDebts(Long clientId);
    DailyIncomeDTO getDailyIncome(int year, int month, int day);
    MonthlyIncomeDTO getMonthlyIncome(int year, int month);
    YearlyIncomeDTO getYearlyIncome(int year);
}