package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Suppliers;
import com.alonsocorporation.pointofsale.exceptions.SupplierAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.SupplierNotFoundException;
import com.alonsocorporation.pointofsale.repositories.SuppliersRepository;
import com.alonsocorporation.pointofsale.services.SuppliersService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersServiceImpl implements SuppliersService {

    private final SuppliersRepository suppliersRepository;

    public SuppliersServiceImpl(SuppliersRepository suppliersRepository) {
        this.suppliersRepository = suppliersRepository;
    }

    @Override
    public List<Suppliers> getAll() {
        return suppliersRepository.findAll();
    }

    @Override
    public Suppliers getById(Long id) {
        return suppliersRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @Override
    public Suppliers create(Suppliers suppliers) {
        // Verificar si ya existe un proveedor con el mismo email
        suppliersRepository.findByEmail(suppliers.getEmail()).ifPresent(existingSupplier -> {
            throw new SupplierAlreadyExistsException(suppliers.getEmail());
        });

        return suppliersRepository.save(suppliers);
    }

    @Override
    public Suppliers update(Long id, Suppliers suppliers) {
        return suppliersRepository.findById(id)
                .map(existingSupplier -> {
                    existingSupplier.setName(suppliers.getName());
                    existingSupplier.setEmail(suppliers.getEmail());
                    existingSupplier.setPhone(suppliers.getPhone());
                    existingSupplier.setAddress(suppliers.getAddress());
                    existingSupplier.setCity(suppliers.getCity());
                    existingSupplier.setState(suppliers.getState());
                    existingSupplier.setZipCode(suppliers.getZipCode());
                    existingSupplier.setCountry(suppliers.getCountry());
                    existingSupplier.setTaxId(suppliers.getTaxId());
                    existingSupplier.setWebsite(suppliers.getWebsite());

                    return suppliersRepository.save(existingSupplier);
                })
                .orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        if (!suppliersRepository.existsById(id)) {
            throw new SupplierNotFoundException(id);
        }
        suppliersRepository.deleteById(id);
    }
}
