package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import com.alonsocorporation.pointofsale.exceptions.ClientNotFoundException;
import com.alonsocorporation.pointofsale.repositories.DataPointOfSaleRepository;
import com.alonsocorporation.pointofsale.services.DataPointOfSaleService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DataPointOfSaleServiceImpl implements DataPointOfSaleService {

    private final DataPointOfSaleRepository dataPointOfSaleRepository;

    public DataPointOfSaleServiceImpl(DataPointOfSaleRepository dataPointOfSaleRepository) {
        this.dataPointOfSaleRepository = dataPointOfSaleRepository;
    }

    @Override
    public List<DataPointOfSale> getAll() {
        return dataPointOfSaleRepository.findAll();
    }

    @Override
    public DataPointOfSale getById(Long id) {
        return dataPointOfSaleRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public DataPointOfSale update(Long id, DataPointOfSale dataPointOfSale) {
        if (id != 1) {
            throw new IllegalArgumentException("Solo se puede actualizar el registro con ID 1");
        }

        DataPointOfSale existingData = dataPointOfSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        existingData.setName(dataPointOfSale.getName());
        return dataPointOfSaleRepository.save(existingData);
    }
}