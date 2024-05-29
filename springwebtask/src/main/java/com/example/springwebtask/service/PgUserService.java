package com.example.springwebtask.service;

import com.example.springwebtask.Exception.ProductNotFoundException;
import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.UserRecord;
import com.example.springwebtask.repository.ProductDao;
import com.example.springwebtask.repository.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Provider;

@Service
@AllArgsConstructor
public class PgUserService implements UserService{

    @Autowired
    private UserDao userDao;
    @Override
    public UserRecord findUser(String loginId, String password){
        if (userDao.findUser(loginId,password) == null) throw new ProductNotFoundException();

        else {return userDao.findUser(loginId,password);}
//        return userDao.findUser(loginId,password);
    }
}
