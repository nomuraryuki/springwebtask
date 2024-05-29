package com.example.springwebtask.repository;

import com.example.springwebtask.record.CategoriesRecord;

import java.util.List;

public interface CategoriesDao {

    List<CategoriesRecord> findAll();
}
