package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import com.alonsocorporation.pointofsale.exceptions.ClientNotFoundException;
import com.alonsocorporation.pointofsale.repositories.DataPointOfSaleRepository;
import com.alonsocorporation.pointofsale.services.DataPointOfSaleService;
import java.util.List;
import java.util.Optional;

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
public DataPointOfSale update(Long id, DataPointOfSale dataPointOfSaleDetails) {
    if (id != 1) {
        throw new IllegalArgumentException("Solo se puede actualizar el registro con ID 1");
    }

    Optional<DataPointOfSale> dataOptional = dataPointOfSaleRepository.findById(id);
    if (dataOptional.isPresent()) {
        DataPointOfSale existingData = dataOptional.get();

        // Actualizar solo los campos que no son null
        if (dataPointOfSaleDetails.getName() != null) {
            existingData.setName(dataPointOfSaleDetails.getName());
        }
        if (dataPointOfSaleDetails.getAddress() != null) {
            existingData.setAddress(dataPointOfSaleDetails.getAddress());
        }
        if (dataPointOfSaleDetails.getPhone() != null) {
            existingData.setPhone(dataPointOfSaleDetails.getPhone());
        }

        // Si el valor de printTicket no es null, actualiza el campo
        if (dataPointOfSaleDetails.getPrintTicket() != null) {
            existingData.setPrintTicket(dataPointOfSaleDetails.getPrintTicket());
        }

        return dataPointOfSaleRepository.save(existingData);
    } else {
        throw new RuntimeException("Registro no encontrado con id " + id);
    }
}

}