package com.example.springwebtask.service;

import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;

import java.util.List;

public interface ProductService {

    List<ProductRecord> findAll();

    ProductRecord findById(int id);

    int insert(InsertProduct product);
    int update(ProductRecord productRecord);

    int delete(int id);

}