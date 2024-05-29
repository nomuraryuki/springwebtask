package com.example.springwebtask.repository;

import com.example.springwebtask.record.CategoriesRecord;
import com.example.springwebtask.record.ProductRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PgCategoriesDao implements CategoriesDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public List<CategoriesRecord> findAll(){
        return jdbcTemplate.query("SELECT * FROM categories ORDER BY id",
                new DataClassRowMapper<>(CategoriesRecord.class));

    }

}
