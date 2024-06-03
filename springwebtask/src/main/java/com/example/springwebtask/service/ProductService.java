package com.example.springwebtask.service;

import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.UpdateProductRecord;

import java.util.List;

public interface ProductService {

    List<ProductRecord> findAll();

    ProductRecord findById(int id);

    int insert(InsertProduct product);
    int update(UpdateProductRecord updateProductRecord);

    int delete(int id);

    List<ProductRecord> searchName(String name);

    ProductRecord findProductIdSuffer(String product_id);

}