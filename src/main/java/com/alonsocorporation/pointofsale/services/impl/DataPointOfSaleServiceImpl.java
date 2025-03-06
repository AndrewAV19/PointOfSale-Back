package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import com.alonsocorporation.pointofsale.repositories.DataPointOfSaleRepository;
import com.alonsocorporation.pointofsale.services.DataPointOfSaleService;
import org.springframework.stereotype.Service;

@Service
public class DataPointOfSaleServiceImpl implements DataPointOfSaleService {

    private final DataPointOfSaleRepository dataPointOfSaleRepository;

      public DataPointOfSaleServiceImpl(DataPointOfSaleRepository dataPointOfSaleRepository) {
        this.dataPointOfSaleRepository = dataPointOfSaleRepository;
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