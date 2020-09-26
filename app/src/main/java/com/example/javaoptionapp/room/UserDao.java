package com.example.javaoptionapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao// Database aceess object 访问数据库操作的接口
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("DELETE FROM users")
    public void nukeTable();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User... user);

    @Delete
    void delete(User user);
}