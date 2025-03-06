package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.entities.DataPointOfSale;

public interface DataPointOfSaleService {
    
    DataPointOfSale update(Long id, DataPointOfSale dataPointOfSale);

}