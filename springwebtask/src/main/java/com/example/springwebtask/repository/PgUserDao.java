package com.example.springwebtask.repository;

import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PgUserDao implements UserDao{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserRecord findUser(String loginId, String password) {
        var param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);
        param.addValue("password", password);
        var list = jdbcTemplate.query("SELECT * FROM users WHERE login_id = :loginId AND password = :password" , param, new DataClassRowMapper<>(UserRecord.class));
        return list.isEmpty() ? null : list.get(0);

    }
}
