package com.example.springwebtask.service;

import com.example.springwebtask.record.ProductRecord;
import com.example.springwebtask.record.UserRecord;
import java.util.List;
public interface UserService {
    UserRecord findUser(String loginId, String password);
}
