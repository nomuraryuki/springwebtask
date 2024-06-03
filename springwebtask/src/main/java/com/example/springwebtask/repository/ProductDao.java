package com.example.springwebtask.repository;

import com.example.springwebtask.record.CategoriesRecord;
import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.UpdateProductRecord;

import java.util.List;

public interface ProductDao {
    List<ProductRecord> findAll();

    ProductRecord findById(int id);

//    ProductRecord checkProductName(String name);

    int insert(InsertProduct insertProduct);
    int update(UpdateProductRecord updateProductRecord);

    int delete(int id);

    List<ProductRecord> searchName(String name);

    ProductRecord findProductIdSuffer(String product_id);


}
