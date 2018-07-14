package com.selton.hellossm.service.serviceimpl;

import com.selton.hellossm.dao.UserDao;
import com.selton.hellossm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public boolean loginUserStatus(String name, String password) {

        if(name == null || "".equals(name)){
            return false;
        }

        if(password == null || "".equals(password)){
            return false;
        }
        String passwordByName = userDao.getPasswordByName(name);
     
        if (password == null){
            return false;
        }

        if (password.equals(passwordByName)) {

            return true;
        }
        return false;
    }
}
