package com.example.springwebtask.repository;

import com.example.springwebtask.record.CategoriesRecord;
import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;

import java.util.List;

public interface ProductDao {
    List<ProductRecord> findAll();

    ProductRecord findById(int id);

    int insert(InsertProduct insertProduct);
    int update(ProductRecord productRecord);

    int delete(int id);


}
