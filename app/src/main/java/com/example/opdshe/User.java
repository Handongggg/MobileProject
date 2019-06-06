package com.example.opdshe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    String id;
    ArrayList<String> post_list;

    public User(){}

    public User(String id , ArrayList<String> post_list){
        this.id=id;
        this.post_list=post_list;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("post_list", post_list);
        return result;
    }
}
