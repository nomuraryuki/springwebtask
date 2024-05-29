package com.example.springwebtask.service;
//import com.example.productweb.Exception.ProductNotFoundException;
import com.example.springwebtask.Exception.ProductNotFoundException;
import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.repository.ProductDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PgProductService implements ProductService {

    private ProductDao productDao;


    @Override
    public List<ProductRecord> findAll() {
        return productDao.findAll();
    }

    @Override
    public ProductRecord findById(int id) {

        if (productDao.findById(id) == null) throw new ProductNotFoundException();

        else {return productDao.findById(id);}
    }

    @Override
    public int insert(InsertProduct insertProduct) {
        return productDao.insert(insertProduct);
    }

    @Override
    public int update(ProductRecord productRecord) {
        return productDao.update(productRecord);
    }

    @Override
    public int delete(int id) {
        return productDao.delete(id);
    }

}
