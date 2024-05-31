package com.example.springwebtask.service;

import com.example.springwebtask.record.CategoriesRecord;
import com.example.springwebtask.record.ProductRecord;

import java.util.List;

public interface CategoriesService {
    List<CategoriesRecord> findAll();

    CategoriesRecord findIdByName(String name);
}
