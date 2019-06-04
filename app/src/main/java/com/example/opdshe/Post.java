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
    public String current_personnel="1";
    public String password;
    public String editor_id;

    public Post(){}
    public Post(String title, String source, String dest, String time, String personnel, String current_personnel, String password, String editor_id){
        this.title=title;
        this.source=source;
        this.dest=dest;
        this.time=time;
        this.personnel=personnel;
        this.current_personnel=current_personnel;
        this.password=password;
        this.editor_id=editor_id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("source", source);
        result.put("dest", dest);
        result.put("time", time);
        result.put("personnel", personnel);
        result.put("current_personnel", current_personnel);
        result.put("password", password);
        result.put("editor_id", editor_id);
        return result;
    }


}
