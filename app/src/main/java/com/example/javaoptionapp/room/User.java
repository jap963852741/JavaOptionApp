package com.example.javaoptionapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    public User(){

    }

    public User(String a,String b){
        this.firstName = a;
        this.lastName = b;
    }
    @PrimaryKey(autoGenerate = true) // 设置主键，并且自动生长
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;
}