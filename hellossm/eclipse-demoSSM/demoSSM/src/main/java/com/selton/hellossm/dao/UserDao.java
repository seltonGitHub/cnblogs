package com.selton.hellossm.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    @Select("SELECT password FROM user WHERE name = #{name}")
    String getPasswordByName(String name);
}
