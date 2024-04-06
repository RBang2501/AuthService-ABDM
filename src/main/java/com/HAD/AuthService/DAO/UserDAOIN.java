package com.HAD.AuthService.DAO;

import com.HAD.AuthService.Model.User;

import java.util.List;

public interface UserDAOIN {

    User save(User user);

    User findById(long id);

    User findByEmail(String email);

    List<User> findAll();

    User update(User user);

    void delete(User user);
}
