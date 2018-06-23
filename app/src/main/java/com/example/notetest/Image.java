package com.example.notetest;

/**
 * Created by 王将 on 2018/5/21.
 */

public class Image {
    private int id;
    private String path;

    public Image(int id,String path){
        this.id=id;
        this.path=path;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
