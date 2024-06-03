package com.example.springwebtask.repository;

import com.example.springwebtask.record.InsertProduct;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.UpdateProductRecord;
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
        var list = jdbcTemplate.query("SELECT products.id, product_id, categories.name AS category, products.name AS name, price, description FROM products JOIN categories ON category_id = categories.id WHERE products.id = :id ORDER BY products.id" ,
                param, new DataClassRowMapper<>(ProductRecord.class));
        return list.isEmpty() ? null : list.get(0);

    }

    @Override
    public int insert(InsertProduct insertProduct) {
        var param = new MapSqlParameterSource();

        param.addValue("product_id", insertProduct.product_id());
        param.addValue("category_id", insertProduct.category_id());
        param.addValue("name", insertProduct.name());
        param.addValue("price", insertProduct.price());
        param.addValue("description", insertProduct.description());

//        return jdbcTemplate.update("INSERT INTO products (product_id,category_id,name,price,description) VALUES( :product_id,:category_id,:name,:price,:description)", param);
        try {
            return jdbcTemplate.update("INSERT INTO products (product_id,category_id,name,price,description) VALUES( :product_id,:category_id,:name,:price,:description)", param);
        }catch (RuntimeException e){
            return -1;
        }

    }

    @Override
    public int update(UpdateProductRecord updateProductRecord){
        var param = new MapSqlParameterSource();

        param.addValue("id", updateProductRecord.id());
        param.addValue("product_id", updateProductRecord.product_id());
        param.addValue("category_id", updateProductRecord.category_id());
        param.addValue("name", updateProductRecord.name());
        param.addValue("price", updateProductRecord.price());
        param.addValue("description", updateProductRecord.description());
        return jdbcTemplate.update("UPDATE products SET product_id = :product_id,category_id = :category_id,name = :name, price = :price,description=:description WHERE id = :id", param);
    }

    @Override
    public int delete(int id){
        var param = new MapSqlParameterSource();

        param.addValue("id", id);

        return jdbcTemplate.update("DELETE from products WHERE id = :id", param);
    }

    @Override
    public List<ProductRecord> searchName(String name){
        var param = new MapSqlParameterSource();

        param.addValue("name", "%"+name+"%");

        var list = jdbcTemplate.query("SELECT products.id, product_id, categories.name AS category, products.name AS name, price, description FROM products JOIN categories ON category_id = categories.id WHERE products.name LIKE :name ORDER BY id;", param
            ,new DataClassRowMapper<>(ProductRecord.class));

        return list.isEmpty() ? null : list;
    }

    @Override
    public  ProductRecord findProductIdSuffer(String product_id){
        var param = new MapSqlParameterSource();
        param.addValue("product_id", product_id);
        var list = jdbcTemplate.query("SELECT products.id, product_id, categories.name AS category, products.name AS name, price, description FROM products JOIN categories ON category_id = categories.id WHERE product_id = :product_id ORDER BY products.id" ,
                param, new DataClassRowMapper<>(ProductRecord.class));
        return list.isEmpty() ? null : list.get(0);

    }
}
