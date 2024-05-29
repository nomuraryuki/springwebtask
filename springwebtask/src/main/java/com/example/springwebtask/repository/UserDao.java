package com.example.springwebtask.repository;

import com.example.springwebtask.record.UserRecord;

public interface UserDao {
    UserRecord findUser(String loginId, String passeord);
}
