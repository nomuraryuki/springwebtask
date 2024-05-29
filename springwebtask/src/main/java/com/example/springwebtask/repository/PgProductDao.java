package com.example.springwebtask.repository;

import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PgProductDao implements ProductDao{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<ProductRecord> findAll() {
        return jdbcTemplate.query("SELECT products.id, product_id, categories.name AS category, products.name AS name, price, description FROM products JOIN categories ON category_id = categories.id ORDER BY id",
                new DataClassRowMapper<>(ProductRecord.class));
    }

    @Override
    public ProductRecord findById(int id) {
        var param = new MapSqlParameterSource();
        param.addValue("id", id);
        var list = jdbcTemplate.query("SELECT * FROM products WHERE id = :id", param, new DataClassRowMapper<>(ProductRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int insert(InsertProduct insertProduct) {
        var param = new MapSqlParameterSource();

        param.addValue("product_id", insertProduct.product_id());
        param.addValue("category_id", insertProduct.category_id());
        param.addValue("name", insertProduct.name());
        param.addValue("price", insertProduct.price());

//        return jdbcTemplate.update("INSERT INTO products (product_id,category_id,name,price,description) VALUES( :product_id,:category_id,:name,:price,:description)", param);

        return jdbcTemplate.update("INSERT INTO products (product_id,category_id,name,price) VALUES( :product_id,:category_id,:name,:price)", param);


    }

    @Override
    public int update(ProductRecord productRecord){
        var param = new MapSqlParameterSource();

        param.addValue("id", productRecord.product_id());
        param.addValue("name", productRecord.name());
        param.addValue("price", productRecord.price());

        return jdbcTemplate.update("UPDATE products SET name = :name, price = :price WHERE id = :id", param);
    }

    @Override
    public int delete(int id){
        var param = new MapSqlParameterSource();

        param.addValue("id", id);

        return jdbcTemplate.update("DELETE from products WHERE id = :id", param);
    }
}
