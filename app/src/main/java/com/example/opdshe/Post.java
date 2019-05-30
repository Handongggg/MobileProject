package com.example.opdshe;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String title;
    public String source;
    public String dest;
    public String time;
    public String personnel;
    public String password;

    public Post(){}
    public Post(String title, String source, String dest, String time, String personnel, String password){
        this.title=title;
        this.source=source;
        this.dest=dest;
        this.time=time;
        this.personnel=personnel;
        this.password=password;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("source", source);
        result.put("dest", dest);
        result.put("time", time);
        result.put("personnel", personnel);
        result.put("password", password);
        return result;
    }


}
