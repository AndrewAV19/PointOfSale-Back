package com.alonsocorporation.pointofsale.services.impl;

import com.alonsocorporation.pointofsale.entities.Suppliers;
import com.alonsocorporation.pointofsale.exceptions.SupplierAlreadyExistsException;
import com.alonsocorporation.pointofsale.exceptions.SupplierNotFoundException;
import com.alonsocorporation.pointofsale.repositories.SuppliersRepository;
import com.alonsocorporation.pointofsale.services.SuppliersService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Suppliers update(Long id, Suppliers supplierDetails) {
        Optional<Suppliers> supplierOptional = suppliersRepository.findById(id);
        if (supplierOptional.isPresent()) {
            Suppliers supplier = supplierOptional.get();

            // Actualizar solo los campos que no son null
            if (supplierDetails.getName() != null) {
                supplier.setName(supplierDetails.getName());
            }
            if (supplierDetails.getContactName() != null) {
                supplier.setContactName(supplierDetails.getContactName());
            }
            if (supplierDetails.getEmail() != null) {
                supplier.setEmail(supplierDetails.getEmail());
            }
            if (supplierDetails.getPhone() != null) {
                supplier.setPhone(supplierDetails.getPhone());
            }
            if (supplierDetails.getAddress() != null) {
                supplier.setAddress(supplierDetails.getAddress());
            }
            if (supplierDetails.getCity() != null) {
                supplier.setCity(supplierDetails.getCity());
            }
            if (supplierDetails.getState() != null) {
                supplier.setState(supplierDetails.getState());
            }
            if (supplierDetails.getZipCode() != null) {
                supplier.setZipCode(supplierDetails.getZipCode());
            }
            if (supplierDetails.getCountry() != null) {
                supplier.setCountry(supplierDetails.getCountry());
            }
            if (supplierDetails.getTaxId() != null) {
                supplier.setTaxId(supplierDetails.getTaxId());
            }
            if (supplierDetails.getWebsite() != null) {
                supplier.setWebsite(supplierDetails.getWebsite());
            }

            return suppliersRepository.save(supplier);
        } else {
            throw new RuntimeException("Supplier not found with id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!suppliersRepository.existsById(id)) {
            throw new SupplierNotFoundException(id);
        }
        suppliersRepository.deleteById(id);
    }
}
