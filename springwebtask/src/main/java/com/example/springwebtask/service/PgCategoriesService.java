package com.example.springwebtask.service;

import com.example.springwebtask.record.CategoriesRecord;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.repository.CategoriesDao;
import com.example.springwebtask.repository.ProductDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PgCategoriesService implements  CategoriesService{

    @Autowired
    private CategoriesDao categoriesDao;

    @Override
    public List<CategoriesRecord> findAll(){
        return categoriesDao.findAll();
    }


}
