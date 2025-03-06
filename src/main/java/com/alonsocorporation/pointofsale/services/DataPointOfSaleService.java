package com.alonsocorporation.pointofsale.services;

import java.util.List;
import com.alonsocorporation.pointofsale.entities.DataPointOfSale;

public interface DataPointOfSaleService {
    
    List<DataPointOfSale> getAll();
    DataPointOfSale getById(Long id);
    DataPointOfSale update(Long id, DataPointOfSale dataPointOfSale);

}