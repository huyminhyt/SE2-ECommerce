package com.project.nike.service;

import com.project.nike.dto.UserDto;
import com.project.nike.model.UserRecord;

import java.util.List;
public interface IUserService {
    void add(UserDto userDto);
    List<UserRecord> getAllUsers();
    void delete(String email);
//    User getUserByEmail(String email);
//    User update(User user);
}



